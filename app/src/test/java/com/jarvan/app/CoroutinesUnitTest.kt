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

    // ===========================================================================
    // Kotlin 语言中文站官方示例 - Start
    // ===========================================================================

    // 协程基础
    @Test
    fun testKotlinCouroutines001() {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
    }

    @Test
    fun testKotlinCoroutine002() {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主线程中的代码会立即执行
        // 调用了 runBlocking 的主线程会一直阻塞直到 runBlocking 内部的协程执行完毕。
        runBlocking {     // 但是这个表达式阻塞了主线程
            delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
        }
    }

    @Test
    fun testKotlinCoroutine003() = runBlocking { // 开始执行主协程
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
    }

    @Test
    fun testKotlinCoroutine004() = runBlocking {
        val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
            delay(1000L)
            println("World!")
        }
        println("Hello,")
        job.join() // 等待直到子协程执行结束
    }


    @Test
    fun testKotlinCoroutine005() = runBlocking {
        launch { // 在 runBlocking 作用域中启动一个新协程
            delay(1000L)
            println("World!")
        }
        println("Hello,")
    }

    @Test
    fun testKotlinCoroutine006() = runBlocking {
        launch {
            delay(200L)
            println("Task from runBlocking")
        }
        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }
            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }
        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }

    @Test
    fun testKotlinCoroutine007() = runBlocking {
        launch { doWorld() }
        println("Hello,")
    }

    // 这是你的第一个挂起函数
    suspend fun doWorld() {
        delay(1000L)
        println("World!")
    }

    @Test
    fun testKotlinCoroutine008() = runBlocking {
        repeat(100_000) { // 启动大量的协程
            launch {
                delay(1000L)
                print(".")
            }
        }
    }

    @Test
    fun testKotlinCoroutine009() = runBlocking {
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 在延迟后退出
    }

    // ===========================================================================
    // Kotlin 语言中文站官方示例 - End
    // ===========================================================================
}
