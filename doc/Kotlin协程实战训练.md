#### Kotlin 协程实战训练

###### 一、Kotlin 协程是什么

- 协程是一种在程序中处理并发任务的方案；也是这种方案的一个组件
- 协程和线程属于一个层级的概念
  - 协程中不存在线程，也不存在并行——并行不是并发哟
- Kotlin 的协程不需要关心这些
  - 因为 Kotlin for Java 的协程并不属于广义的协程
- Kotlin 协程是一个线程框架
- Kotlin 协程可以用看起来同步的代码写出实质上异步的操作
- 关键：线程的自动切回

###### 二、协程的代码怎么写

- 用 launch() 来开启一段协程，一般需要指定 Dispatchers.Main

- 把要在后台工作的函数，写成 suspend函数。需要在内部调用其他 suspend 函数来真正切线程

- 按照一条线写下来，线程会自动切换

  ```
  GlobalScope.launch {
  	println("Coroutines Camp ${Thread.currentThread().name}")
  }
  
  GlobalScope.launch(Dispatchers.Main) {
  	withContext(Dispatchers.IO) {
          delay(1000)
          println("Coroutines Camp io ${Thread.currentThread().name}")
      }
      println("Coroutines Camp ${Thread.currentThread().name}")
  }
  ```

- 协程的额外天然优势：性能！很方便将耗时操作放在后台

###### 三、suspend

- 并不是用来切线程的
- 语法上是标记和提醒的作用：标记函数是挂起函数，是耗时的，需要在协程中调用，需要协程上下文
- 编译过程也起到一定的作用

###### 四、协程和线程

###### 五、协程和 RxJava

###### 六、Retrofit 对协程的支持