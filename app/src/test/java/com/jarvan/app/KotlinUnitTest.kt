package com.jarvan.app

import org.junit.Test

class KotlinUnitTest {
    val str: String = "str(class val)"

    @Test
    fun playWithStandard() {
        val str = "str(function val)"
        var number = 0
        var ret = 0

        println("run:")
        ret = run {
            number++
            println("str = $str")
            println("this = $this")
            println("this.str = ${this.str}")
            0
        }
        println("ret = $ret, number = $number")

        println("T.run:")
        ret = number.run {
            number++
            println("str = $str")
            println("this = $this")
            1
        }
        println("ret = $ret, number = $number")

        println("T.let:")
        ret = number.let {
            println("str = $str")
            println("it = $it")
            println("this = $this")
            println("this.str = ${this.str}")
            2
        }
        println("ret = $ret, number = $number")

        println("with(T):")
        ret = with(number) {
            number++
            println("str = $str")
            println("this = $this")
            3
        }
        println("ret = $ret, number = $number")

        println("apply:")
        ret = number.apply {
            number++
            println("str = $str")
            println("this = $this")
            4
        }
        println("ret = $ret, number = $number")

        println("also:")
        ret = number.also {
            number++
            println("str = $str")
            println("it = $it")
            println("this = $this")
            println("this.str = ${this.str}")
            5
        }
        println("ret = $ret, number = $number")
    }
}
