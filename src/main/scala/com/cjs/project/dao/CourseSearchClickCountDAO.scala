package com.cjs.project.dao

import com.cjs.project.dao.CourseClickCountDAO.{cf, qualifer}
import com.cjs.project.domain.CourseSearchClickCount
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes
import project.util.HBaseUtils

import scala.collection.mutable.ListBuffer

object CourseSearchClickCountDAO {
    val tableName = "immoc_course_search_clickcount"
    val cf = "info"
    val qualifer = ""

    def save(list:ListBuffer[CourseSearchClickCount])= {
        val table = HBaseUtils.getInstance().getTable(tableName)

        for (ele <- list) {
            table.incrementColumnValue(Bytes.toBytes(ele.day_search_course),
                Bytes.toBytes(cf),
                Bytes.toBytes(qualifer),
                ele.click_count)
        }
    }

    def count(day_search_course:String) = {
        val table = HBaseUtils.getInstance().getTable(tableName)
        val get = new Get(Bytes.toBytes(day_search_course))

        val value = table.get(get).getValue(Bytes.toBytes(cf),Bytes.toBytes(qualifer))
        if (value==null) {
            0l
        }else{
            Bytes.toLong(value)
        }
    }

}
