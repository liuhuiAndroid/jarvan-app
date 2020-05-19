#### Android KTX

Android KTX 是包含在 Android Jetpack 及其他 Android 库中的一组 Kotlin 扩展程序。

它能使 Android 上的 Kotlin 代码更简洁，从而提高开发者的效率和使用体验。

#### Core KTX

Core KTX 模块为属于 Android 框架的通用库提供扩展程序。

[Core KTX 文档](https://developer.android.google.cn/kotlin/ktx#core)

```groovy
    dependencies {
        implementation "androidx.core:core-ktx:1.2.0"
    }
```

方便之处：

1. 字符串转 URI

   ```kotlin
   val myUriString = "https://www.baidu.com"
   // Kotlin
   val uri1 = Uri.parse(myUriString)
   // Kotlin with Android KTX
   val uri2 = myUriString.toUri()
   ```

2. 写入 SharedPreferences

   ```kotlin
   
   ```

3. 监听 onPreDraw 回调

   ```kotlin
   
   ```

#### Collection KTX

Collection 扩展程序包含在 Android 的节省内存的集合库中使用的效用函数，包括 ArrayMap、LongParseArray、LruCache 等等。

```groovy
    dependencies {
        implementation "androidx.collection:collection-ktx:1.1.0"
    }
```

#### Fragment KTX

Fragment KTX 模块提供了一系列扩展程序来简化 Fragment API

```groovy
    dependencies {
        implementation "androidx.fragment:fragment-ktx:1.2.4"
    }
```

#### Activity KTX

```groovy
    dependencies {
        implementation "androidx.activity:activity-ktx:1.1.0"
    }
```

