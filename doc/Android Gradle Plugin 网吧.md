#### Groovy 两个语法点

- getter / setter
  - 每个 field，Groovy 会自动创建它的 getter 和 setter 方法，从外部可以直接调用，并且在使用 object.fieldA 来获取值或者使用 object.fieldA = newValue 来赋值的时候，实际会自动转调用 object.getFieldA() 和 object.setFieldA(newValue)
- 字符串中单双引号
  - 单引号是不不带转义的，而双引号内的内容可以使用 "string1${var}string2"的方式来转义

#### Gradle Plugin

##### 什么是 Gradle Plugin

```groovy
apply plugin: 'com.android.application'
```

本质：把逻辑独立的代码抽取和封装

###### Plugin 的最基本写法

写在 build.gradle 里：

```groovy
class PluginDemo implements Plugin<Project>{
	@override
	void apply(Project project){
		println 'Hello author.'
	}
}

apply plugin: PluginDemo
```

###### Extension

```groovy
class ExtensionDemo{
    def author = 'Kai'
}

class PluginDemo implements Plugin<Project>{
	@override
	void apply(Project project){
        def extension = new ExtensionDemo()
        println "Hello ${extension.author}."
        
		def extension = project.extensions.create('hencoder',ExtensionDemo)
        project.afterEvaluate{
            println "Hello ${extension.author}."
        }
	}
}
apply plugin: PluginDemo

hencoder {
    author 'renwuxian'
}
```

###### 正式的项目：写在 buildSrc 目录下

- main/resources/META-INF/gradle-plugins/*.properties 中的 * 是插件的名称，例如 *.properties 是 com.hencoder.plugindemo.properties，最终在应用插件的代码就应该是：

  ```groovy
  apply plugin: 'com.hencoder.plugindemo'
  ```

- *.properties 中只有一行，格式是：

  ```
  implementation-class=com.hencoder.plugin_demo.DemoPlugin
  ```

  其中等号右边指定了 Plugin 具体是哪个类

- Plugin 和 Extension 写法和在 build.gradle 里的写法一样

- 关于 buildSrc 目录

  - 这是 gradle 的一个特殊目录，这个目录的 build.gradle 会自动被执行，即使不配置进 setting.gradle
  - buildSrc 的执行早于任何一个 project，也早于 setting.gradle。它是一个独立的存在
  - buildSrc 所配置出来的 Plugin 会被自动添加到编译过程中每一个 project 的 classpath，因此它们才可以直接使用 apply plugin: 'xxx' 的方式来便捷应用这些 plugin
  - setting.gradle 中如果配置了 ':buildSrc'，buildSrc 目录就会被当做是子 Project，因此会被执行两遍。所以在 setting.gradle 里面应该删掉 ':buildSrc' 的配置

#### Transform

- 是什么？：是由 Android 提供的，在项目构建过程中把编译后的文件添加自定义中间处理过程的工具

- 怎么写？

  - 先添加依赖

    ```groovy
        repositories {
            google()
            jcenter()
        }
        dependencies {
            classpath 'com.android.tools.build:gradle:3.2.1'
        }
    ```

  - 然后继承 com.android.build.api.transform.Transform，创建一个子类

    ```groovy
    class DemoTransform extends Transform{
        
    }
    ```

  - 还能做什么：修改字节码：上面的这段代码只是把编译完的内容原封不动搬运到目标位置，没有实际用处。要修改字节码，需要引入其他工具，例如：javassist