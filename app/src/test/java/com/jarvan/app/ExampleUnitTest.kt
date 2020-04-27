package com.jarvan.app

import org.junit.Test

import org.junit.Assert.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        var time = switchCreateTime("2020-03-26T19:27:37.197")
        println("time = $time")
    }

    private fun switchCreateTime(createTime: String): String {
        var formatStr2: String = ""
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") //注意格式化的表达式
        try {
            val time: Date = format.parse(createTime)
            formatStr2 = SimpleDateFormat("yyyy-MM-dd HH:mm").format(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return formatStr2
    }
}
