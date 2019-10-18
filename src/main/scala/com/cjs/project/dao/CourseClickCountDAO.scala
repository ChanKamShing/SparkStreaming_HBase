package com.cjs.project.dao

import com.cjs.project.domain.CourseClickCount
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes
import project.util.HBaseUtils

import scala.collection.mutable.ListBuffer

object CourseClickCountDAO {
    val tableName = "immoc_course_clickcount"   // HBase的表明
    val cf = "info"
    val qualifer = ""   // HBase列名

    /**
      * 保存数据到HBase
      * @param list
      */
    def save(list:ListBuffer[CourseClickCount]) = {
        val table = HBaseUtils.getInstance().getTable(tableName)

        for (ele <- list) {
//            自动更新字段的count值
            table.incrementColumnValue(Bytes.toBytes(ele.day_course),
                Bytes.toBytes(cf),
                Bytes.toBytes(qualifer),
                ele.click_count)
        }
    }

    /**
      * 根据Rowkey查询数据
      * @param day_course
      */
    def count(day_course:String) = {
        val table = HBaseUtils.getInstance().getTable(tableName)

        val get = new Get(Bytes.toBytes(day_course))
        val value = table.get(get).getValue(cf.getBytes, qualifer.getBytes)

        if (null == value) {
            0l
        } else {
            Bytes.toLong(value)
        }
    }
}
