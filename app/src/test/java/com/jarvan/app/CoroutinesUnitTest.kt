package com.jarvan.app

import android.icu.util.TimeUnit
import android.util.Log
import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoroutinesUnitTest {

    companion object{
        private const val TAG = "CoroutinesUnitTest"
    }

    @Test
    fun test0(){
        GlobalScope.launch {
            delay(1000L)
            println("Hello,World!")
        }
        Thread.sleep(10000)
    }

    @Test
    fun test1(){
        runBlocking {
            repeat(100_000){
                launch {
                    delay(1000L)
                    print(".")
                }
            }
        }
    }

    @Test
    fun test2(){
        // GlobalScope.launch就是创建一个协程环境并且启动一个协程
        GlobalScope.launch {

        }
        // Dispatchers.Default是更换协程上下文环境
        // 协程上下文包括了一个协程调度器
        val job = GlobalScope.launch(Dispatchers.Default) {
            // 在一个协程环境中，执行后台耗时操作
        }
        job.cancel()
        // newSingleThreadContext("MyThread")是更换协程上下文环境
        // 协程上下文包括了一个协程调度器
        GlobalScope.launch(newSingleThreadContext("MyThread")){
            // 在一个协程环境中，执行后台耗时操作
        }
    }

    @Test
    fun test3(){
        GlobalScope.launch() {
            val result = async {
                // 耗时操作
                Thread.sleep(1000)
                1
            }
            println("result = ${result.await().toString()}")
        }
        Thread.sleep(10000)
    }

    @Test
    fun test4(){
        GlobalScope.launch {
            println("Hello ${Thread.currentThread().name}")
            testChild()
            println("End ${Thread.currentThread().name}")
        }
        Thread.sleep(5000)
    }

    private suspend fun testChild(){
        withContext(Dispatchers.IO){
            println("World ${Thread.currentThread().name}")
        }
    }

}
