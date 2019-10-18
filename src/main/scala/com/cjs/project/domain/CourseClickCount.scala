package com.cjs.project.domain

/**
  * 实战课程点击数
  * @param day_course   HBase的Rowkey
  * @param click_count  对应Rowkey的访问总数
  */
case class CourseClickCount (day_course:String, click_count:Long)
