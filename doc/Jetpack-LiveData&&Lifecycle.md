#### 学习 LiveData 和 Lifecycle

为了帮助开发者更高效、更容易地构建优秀的应用，Google 推出了 Android Jetpack。它包含了开发库、工具、以及最佳实践指南。

Lifecycle 库：它可以有效的避免内存泄漏，和解决 Android 生命周期的常见难题

Lifecycle 库中的 LiveData 类：LiveData 是一种具有生命周期感知能力的可观察数据持有类，LiveData 可以使屏幕上显示的内容与数据随时保持同步。

LiveData 对象通常保存在 ViewModel 类中，假设你正在为某个 User 对象创建 Activity 和 ViewModel，我们将使用一个 UserLiveData 对象来保存这个 User 对象，接下来，在 Activity 的 onCreate 方法中，我们可以从 ViewModel 中获取 LiveData，并在 LiveData 上调用 observe 方法，方法中第一个参数为 Context，第二个参数是一个“Observer 观察者”，属于方法回调，回调之后界面会被更新。如果需要更新 LiveData，可以调用 setValue 或 postValue 方法，两者的区别在于，setValue 只可以在主线程上运行，postValue 只可以在后台线程上运行。调用 setValue 或 postValue 时，LiveData 将通知 Observer，并更新界面。

现支持 LiveData、ViewModel 与数据绑定的搭配使用，通常情况下会在XML布局内绑定 ViewModel，将 ViewModel 和数据绑定布局关联后，添加 binding.setLifecycleOwner(this) 后，即可让XML内绑定的 LiveData 和 Observer 建立观察连接，接下来，在XML中添加对 ViewModel 的引用，LiveData 包含在 ViewModel 之中。如果使用数据绑定，那么就不需要像我们之前介绍的那样，在 LiveData 上调用 observe 方法了，可以在XML中 TextView 上直接引用 LiveData。LiveData 与其他可观察对象的不同之处在于，它可以感知元件的生命周期。因为我们在调用 observe 方法时用了 UI 界面参数，所以 LiveData 了解界面的状态。

LiveData生命周期感知的优势有以下几点：

如果 Activity 不在屏幕上，LiveData 不会触发没必要的界面更新，如果 Activity 已销毁，LiveData 将自动清空与 Observer 的连接。这样，屏幕外或者已销毁的 Activity 或 Fragment，不会被意外地调用。

生命周期感知的实现，得益于 Android Framework使用了以下 Lifecycles 中的类：

Lifecycle：表示 Android 生命周期及状态的对象

LifecycleOwner：用于连接有生命周期的对象，例如 AppCompatActivity 和 ActivityFragment

LifecycleObserver：用于观察 LifecycleOwner 的接口

LiveData 是一个 LifecycleObserver，它可以直接感知 Activity 或 Fragment 的生命周期。

复杂用例：

Room + LiveData：Room 数据框架可以很好地支持 LiveData，Room 返回的 LiveData 对象，可以在数据库数据变化时自动收到通知，还可以在后台线程中加载数据，这样我们可以在数据库更新的时候，轻松地更新界面。

LiveData 的数据转换：

map() 可以转换 LiveData 的输出：可以将输出 LiveData A 的方法传递到 LiveData B

switchMap() 可以更改被 LiveData 观察的对象，switchMap 和 map 很相似，区别在于，switchMap 给出的是 LiveData，而 map 方法给出的是具体值

MediatorLiveData 可以用于自定义转换，它可以添加或者移除原 LiveData 对象，然后多个原 LiveData，可以进行被组合，作为单一 LiveData 向下传递。

[第一步：LiveData 官方文档](<https://developer.android.com/topic/libraries/architecture/livedata>)

[Android 架构组件基本示例](https://github.com/googlesamples/android-architecture-components/tree/master/BasicSample)

[视频 Android Jetpack: LiveData 和 Lifecycle](<https://www.bilibili.com/video/av33633628>)

[《即学即用Android Jetpack - ViewModel & LiveData》](https://www.jianshu.com/p/81a284969f03)

[MVVM项目实战之路-搭建一个登录界面](<https://cloud.tencent.com/developer/article/1153469>)

