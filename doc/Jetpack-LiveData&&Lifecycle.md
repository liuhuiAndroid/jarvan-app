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

-------------

[【AAC 系列二】深入理解架构组件的基石：Lifecycle](https://juejin.im/post/5cd81634e51d453af7192b87)

[【AAC 系列三】深入理解架构组件：LiveData](https://juejin.im/post/5ce54c2be51d45106343179d)

#### 具体使用

- MutableLiveData

  LiveData 的一个最简单实现，它可以接收数据更新并通知观察者

- Transformations#map()

  将数据从一个 LiveData 传递到另一个 LiveData

- MediatorLiveData

  将多个 LiveData 源数据集合起来

- Transformations#switchMap

  用来添加一个新数据源并相应地删除前一个数据源

#### Lifecycle 原理分析

1. 简单使用

   ```kotlin
           lifecycle.addObserver(object : LifecycleObserver {
               
               @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
               fun onResume(){
   
               }
           })
   ```

2. ReportFragment 重写了生命周期回调的方法，Lifecycle 利用 ReportFragment 来实现监听生命周期，并在生命周期回调里调用了内部 dispatch 的方法来分发生命周期事件

3. ComponentActivity#onCreate 方法注入了 ReportFragment，通过 Fragment 来实现生命周期监听

   ```java
   public class ComponentActivity implements LifecycleOwner{
       
       // 负责管理 Observer
       private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
       
       @Override
       protected void onCreate(@Nullable Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           // 注入 ReportFragment
           ReportFragment.injectIfNeededIn(this);
       }
       
       @Override
       protected void onSaveInstanceState(@NonNull Bundle outState) {
           Lifecycle lifecycle = getLifecycle();
           if (lifecycle instanceof LifecycleRegistry) {
               ((LifecycleRegistry) lifecycle).setCurrentState(Lifecycle.State.CREATED);
           }
           super.onSaveInstanceState(outState);
       }
       
       @Override
       public Lifecycle getLifecycle() {
           return mLifecycleRegistry;
       }
   }
   ```

4. LifecycleRegistry#handleLifecycleEvent 方法接收事件

   ```java
   public class LifecycleRegistry extends Lifecycle {
   	// Sets the current state and notifies the observers.
       // 处理状态并通知 observers
       public void handleLifecycleEvent(@NonNull Lifecycle.Event event) {
           State next = getStateAfter(event);
           moveToState(next);
       }
       
   	private void moveToState(State next) {
           sync();
       }
       
       private void sync() {
           while (!isSynced()) {
               if (mState.compareTo(mObserverMap.eldest().getValue().mState) < 0) {
                   backwardPass(lifecycleOwner);
               }
               if (!mNewEventOccurred && newest != null
                       && mState.compareTo(newest.getValue().mState) > 0) {
                   forwardPass(lifecycleOwner);
               }
           }
       }
       
       private void backwardPass(LifecycleOwner lifecycleOwner) {
           Iterator<Entry<LifecycleObserver, ObserverWithState>> descendingIterator =
                   mObserverMap.descendingIterator();
           while (descendingIterator.hasNext() && !mNewEventOccurred) {
               Entry<LifecycleObserver, ObserverWithState> entry = descendingIterator.next();
               ObserverWithState observer = entry.getValue();
               while ((observer.mState.compareTo(mState) > 0 && !mNewEventOccurred
                       && mObserverMap.contains(entry.getKey()))) {
                   Event event = downEvent(observer.mState);
                   pushParentState(getStateAfter(event));
                   // 重点，分发 Event 
                   observer.dispatchEvent(lifecycleOwner, event);
                   popParentState();
               }
           }
       }
       
       static class ObserverWithState {
           LifecycleEventObserver mLifecycleObserver;
           
           void dispatchEvent(LifecycleOwner owner, Event event) {
               // 最终调到这里
               mLifecycleObserver.onStateChanged(owner, event);
           }
       }
   }
   
   class ReflectiveGenericLifecycleObserver implements LifecycleEventObserver {
       private final CallbackInfo mInfo;
       
       ReflectiveGenericLifecycleObserver(Object wrapped) {
           mWrapped = wrapped;
           // 通过反射获取 method 信息
           mInfo = ClassesInfoCache.sInstance.getInfo(mWrapped.getClass());
       }
       
       @Override
       public void onStateChanged(LifecycleOwner source, Event event) {
           // 通过反射调用 method
           mInfo.invokeCallbacks(source, event, mWrapped);
       }
   }
   ```

   我们在 Observer 用注解修饰的方法，会被通过反射的方式获取，并保存下来，然后在生命周期发生改变的时候再找到对应 Event 的方法，通过反射来调用方法。

5. Lifecycle 的生命周期事件与状态的定义

   ```java
   public abstract class Lifecycle {
       // 生命周期事件
       public enum Event {
           ON_CREATE,
           ON_START,
           ON_RESUME,
           ON_PAUSE,
           ON_STOP,
           ON_DESTROY,
           ON_ANY
       }
       // 当前组件的生命周期状态
       public enum State {
           DESTROYED,
           INITIALIZED,
           CREATED,
           STARTED,
           RESUMED;
       }
   }
   ```

6. 

#### Lifecycle 实战应用

- 自动移除 Handler 的消息：LifecycleHandler
- 给 ViewHolder 添加 Lifecycle 的能力



