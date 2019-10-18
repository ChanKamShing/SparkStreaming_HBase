package com.cjs.project.util

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat

object DateUtils {
    val YYYMMDDHHMMSS_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
    val TARGE_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss")

    def getTime(time:String) = {
        YYYMMDDHHMMSS_FORMAT.parse(time).getTime
    }

    def parseToMinute(time:String) = {
//        format:yyyyMMddHHmmss
        TARGE_FORMAT.format(new Date(getTime(time)))
    }

    def main(args: Array[String]): Unit = {
//        2019-10-13 20:56:24,913 [main] [LoggerGenertor] [INFO] - current value is : 2
        println(parseToMinute("2019-10-13 20:56:24"))
    }
}
