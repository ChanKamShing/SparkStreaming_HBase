package com.cjs.project.sparkstreaming

import com.cjs.project.dao.{CourseClickCountDAO, CourseSearchClickCountDAO}
import com.cjs.project.domain.{ClickLog, CourseClickCount, CourseSearchClickCount}
import com.cjs.project.util.DateUtils
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ListBuffer

/**
  * 使用Spark Streaming处理Kafka过来的数据
  *
  * 数据格式：
  * 63.132.29.46	2019-10-15 00:36:16	"GET /class/131.html HTTP/1.1"	404	-
  * 46.98.10.132	2019-10-15 00:36:16	"GET /class/112.html HTTP/1.1"	200	-
  * 46.29.167.10	2019-10-15 00:36:16	"POST /course/list HTTP/1.1"	500	-
  * 63.72.55.156	2019-10-15 00:36:16	"GET /class/130.html HTTP/1.1"	404	-
  * 98.63.187.167	2019-10-15 00:36:16	"GET /learn/821 HTTP/1.1"	200	https://www.sogou.com/web?query=Hadoop基础
  */
object ImmocStateStreamingApp {
    def main(args: Array[String]): Unit = {
        if (args.length != 4) {
            println("Usage: ImmocStateStreamingApp <zkQuorum> <groupId> <topics> <numThreads>")
            System.exit(1)
        }

        val Array(zkQuorum,groupId,topics,numThreads) = args

        val conf = new SparkConf()
            .setAppName("ImmocStateStreamingApp")
            .setMaster("local[2]")
        val ssc = new StreamingContext(conf,Seconds(60))

        val topicMap = topics.split(",").map((_,numThreads.toInt)).toMap

        val messages = KafkaUtils.createStream(ssc = ssc,zkQuorum = zkQuorum, groupId = groupId, topicMap).map(_._2)

//        messages.count().print()

        val cleanData = messages.map{line=>
            val infos = line.split("\t")
            val url = infos(2).split(" ")(1)

            var courseId = 0    // 全部标记为0，当url是/class开头的，才做非0处理

            if (url.startsWith("/class")){
                val courseIDHTML = url.split("/")(2)
                courseId = courseIDHTML.substring(0,courseIDHTML.lastIndexOf(".")).toInt
            }

//            case class ClickLog(ip:String, time:String, courseId:Int, StatusCode:Int, referer:String)
//            创建一个case class，符合业务需求的DStream格式数据
            ClickLog(infos(0), DateUtils.parseToMinute(infos(1)), courseId, infos(3).toInt, infos(4))
        }.filter(clicklog => clicklog.courseId != 0)

        cleanData.map{clickLog=>
//            HBase rowKey设计：20191111_88
            (clickLog.time.substring(0,8)+"_"+clickLog.courseId, 1)
        }.reduceByKey(_+_).foreachRDD{rdd=>
//            DStream => RDD => Partition这种方式是最高效的
            rdd.foreachPartition{partition=>
                val list_courseClickCount = new ListBuffer[CourseClickCount]
//                pair是wordCount的tuple
                partition.foreach{pair=>
                    list_courseClickCount.append(CourseClickCount(pair._1,pair._2))
                }
                CourseClickCountDAO.save(list_courseClickCount)
            }
        }

        cleanData.filter(clickSearchLog=> clickSearchLog.referer != "-")
                .map{clickSearchLog=>
//                    referer =>   https://www.sogou.com/web?query=Hadoop基础
                    (clickSearchLog.time.substring(0,8)+"_"+clickSearchLog.referer.split("/")(2)+"_"+clickSearchLog.courseId,1)
                }.reduceByKey(_+_).foreachRDD{rdd=>
            rdd.foreachPartition{partition=>
                val list_courseSearchClickCount = new ListBuffer[CourseSearchClickCount]
                partition.foreach{pair=>
                    list_courseSearchClickCount.append(CourseSearchClickCount(pair._1,pair._2))
                }
                CourseSearchClickCountDAO.save(list_courseSearchClickCount)
            }
        }

        ssc.start()
        ssc.awaitTermination()
    }
}
