package com.cjs.project.domain

/**
  * 清洗后的日志信息，用户存储到数据库
  * @param ip 日志访问的ip地址
  * @param time 日志访问的时间
  * @param courseId 日志访问的实战课程编号
  * @param StatusCode   日志访问的状态码
  * @param referer  日志访问的referer
  */
case class ClickLog(ip:String, time:String, courseId:Int, StatusCode:Int, referer:String)
