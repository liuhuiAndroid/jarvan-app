#### 学习 WorkManager - 满足您的后台调度需求

你的应用程序不会总处于前台，但你仍需确保那些重要的后台任务将会执行。例如：下载更新、与服务器同步等等。Google 为我们提供了这些现有的 API 来处理后台操作，然而当这些 API 使用不当时，可能会造成这样的结果。那么为了帮助用户省电，Android 在最近几个版本中引入了一系列的电量优化功能，作为一个开发者，您需要了解电量优化的限制，并且妥善处理和使用这些 API，以确保你的后台任务在不同的 API 等级下都可顺利执行。为了避免后台任务无法执行的情况，您可以需要写冗长的代码来支持在不同设备上的用户群。那么为了解决这样的痛点，我们向您推出 WorkManager 库。WorkManager 为后台任务提供了一套统一的解决方案。它可以根据 Android 电量优化以及设备的 API 等级，选择合适的方式执行。WorkManager 属于 Android Jetpack 的一部分，它向后兼容至 API 14，并且对无论集成 Google Play 服务与否的设备都予以支持。在这一基础上，WorkManager 还为您提供了更多的便利功能。使用 WorkManager 管理任务主要有以下两大特点：第一允许延迟，第二保证执行。可延迟的工作是指那些即使不立即执行，也仍然有必要运行的任务。例如，向服务器发送分析数据就是可延迟的任务，但是发送即时消息就不满足这样的条件。保证执行的意思是，即时在应用程序被关闭，或者设备重启的情况下，任务也会照常执行，图片备份就是一个十分典型的 WorkManager 用例。

[Android Jetpack WorkManager](<https://www.bilibili.com/video/av56276889>)

[跟上脚步，进入后台执行新时代](https://mp.weixin.qq.com/s/lvUJEL7PAZFAzNjrscGEuw)

[[译\] 从Service到WorkManager](https://links.jianshu.com/go?to=https%3A%2F%2Fjuejin.im%2Fpost%2F5b04d064f265da0b80711759%23heading-3)

[《即学即用Android Jetpack - WorkManger》](https://www.jianshu.com/p/68e720b8a939)

#### WorkManager 介绍

WorkManager 可以自动维护后台任务的执行时机，执行顺序，执行状态。

WorkManager 任务构建可以解决任务顺序问题

```
WorkContinuation left, right;
left = workManager.beginWith(A).then(B);
right = workManager.beginWith(C).then(D);
WorkContinuation.combine(Arrays.asList(left,right)).then(E).Enqueue();
```

WorkManager 任务状态通知

WorkManager 任务控制

```java
cancelWorkById(UUID)      // 通过 ID 取消单个任务
cancelAllWorkByTag(String)// 通过 Tag 取消所有任务
cancelUniqueWork(String)  // 通过名字取消唯一任务
```

WorkManager 核心类

- Work

  任务的执行者，是一个抽象类，需要继承它实现要执行的任务

- WorkRequest

  指定让哪个 Work 执行任务，指定执行的环境，执行的顺序等。要使用它的子类 OneTimeWorkRequest 或 PeriodicWorkRequest

- WorkManager

  管理任务请求和任务队列，发起的 WorkRequest 会进入它的任务队列

- WorkStatus

  包含有任务的状态和任务的信息，以 LiveData 的形式提供给观察者

WorkManager 简单使用

```java
//1.编写一个UploadFileWorker.class 继承自 Worker,创建任务
//2.创建输入参数
Data inputData = new Data.Builder()
     .putString("key", "value")
     .putBoolean("key1", false)
     .putStringArray("key2", new String[]{})
     .build();

// 编写约束条件
Constraints constraints = new Constraints();
//设备存储空间充足的时候 才能执行 ,>15%
constraints.setRequiresStorageNotLow(true);
//必须在执行的网络条件下才能好执行,不计流量 ,wifi
constraints.setRequiredNetworkType(NetworkType.UNMETERED);
//设备的充电量充足的才能执行 >15%
constraints.setRequiresBatteryNotLow(true);
//只有设备在充电的情况下 才能允许执行
constraints.setRequiresCharging(true);
//只有设备在空闲的情况下才能被执行 比如息屏，cpu利用率不高
constraints.setRequiresDeviceIdle(true);
//workmanager利用contentObserver监控传递进来的这个uri对应的内容是否发生变化,当且仅当它发生变化了，我们的任务才会被触发执行，以下三个api是关联的
constraints.setContentUriTriggers(null);
//设置从content变化到被执行中间的延迟时间，如果在这期间。content发生了变化，延迟时间会被重新计算
constraints.setTriggerContentUpdateDelay(0);
//设置从content变化到被执行中间的最大延迟时间
constraints.setTriggerMaxContentDelay(0);

//3.创建workrequest
OneTimeWorkRequest request = new OneTimeWorkRequest
      .Builder(UploadFileWorker.class)
      .setInputData(inputData)
      //其它许许多多条件约束
      .setConstraints(constraints)
      //设置一个拦截器，在任务执行之前可以做一次拦截，去修改入参的数据然后返回新的数据交由worker使用
      .setInputMerger(null)
      //当一个任务被调度失败后，所要采取的重试策略，可以通过BackoffPolicy来执行具体的策略，每隔10秒重试一次
      .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
      //任务被调度执行的延迟时间
      .setInitialDelay(10, TimeUnit.SECONDS)
      //设置该任务执行失败，尝试执行的最大次数
      .setInitialRunAttemptCount(2)
      //设置这个任务开始执行的时间
      .setPeriodStartTime(0, TimeUnit.SECONDS)
      //指定该任务被调度的时间
      .setScheduleRequestedAt(0, TimeUnit.SECONDS)
      //当一个任务执行状态编程finish时，又没有后续的观察者来消费这个结果，难么workamnager会在内存中保留一段时间的该任务的结果。超过这个时间，这个结果就会被存储到数据库中下次想要查询该任务的结果时，会触发workmanager的数据库查询操作，可以通过uuid来查询任务的状态
      .keepResultsForAtLeast(10, TimeUnit.SECONDS)
      .build();

// 4.加入队列
List<OneTimeWorkRequest> workRequests = new ArrayList<>();
workRequests.add(request);
WorkContinuation continuation = WorkManager.getInstance().beginWith(workRequests).enqueue();

// 5.观察执行状态 及结果      
continuation.getWorkInfosLiveData().observe(this, new Observer<List<WorkInfo>>() {
    @Override
    public void onChanged(List<WorkInfo> workInfos) {
        //监听任务执行的结果
        for (WorkInfo workInfo : workInfos) {
            WorkInfo.State state = workInfo.getState();
        } 
    }
 });
```

WorkManager 使用场景 TODO

#### WorkManager 原理分析

WorkManager 架构图 TODO 

