#### gradle 是什么

- gradle 是构建工具，不是语言
- gradle 用了 Groovy 语言，创造了一种 DSL，但它本身不是语言

#### 怎么构建

按照 gradle 的规则构建

- build.gradle

  - buildscript：配置 plugin
    - repositories：配置依赖的仓库地址
    - dependencies：配置 plugin 依赖
      - classpath：相当于 add('classpath','xxx')
  - allprojects：配置 module
    - repositories：配置依赖的仓库地址

- app.gradle

  - 区分 debug 和 release

    ```java
    // main 同级建立 debug 包，下面放 BuildTypeUtils.java，写 debug 的代码
    // main 同级建立 release 包，下面放 BuildTypeUtils.java，写 release 的代码
    ```

- setting.gradle

- gradle-wrapper

- gradle 语法 

#### 闭包

- 相当于可以传递的代码块

#### buildType 和 produceFlavors

Android 打渠道包

```
flavorDimensions 'china', 'nation'
productFlavors {
	free{
		dimension 'china'
	}
	china{
		dimension 'nation'
	}
}
```

#### compile，implementation 和 api

- implementation：不会传递依赖
- compile / api：会传递依赖；api 是 compile 的替代品，效果完全相同
- 当依赖被传递时，二级依赖的改动会导致0级项目重新编译；当依赖不传递时，二级依赖的改动不会导致0级项目重新编译

#### gradle-wrapper

帮助不需要安装 gradle 也可以运行 gradle 项目

```
gradle wrapper

gradlew assemble
```

#### 项目结构

- 单 project
- 多 project

#### task

- 使用方法

  ```
  gradlew taskName
  ```

- task 的结构

  ```
  task taskName{
  	初始化代码
  	doFirst{
  		task 代码
  	}
  	doLast{
  		task 代码
  	}
  }
  ```

- doFirst doLast 和普通代码段的区别

  - 普通代码段：在 task 创建过程中就会被执行，发生在 configuration 阶段
  - doFirst doLast：在 task 执行过程中被执行，发生在 execution 阶段。如果用户没有直接或间接执行 task，那么它的 doFirst doLast 代码不会被执行
  - doFirst doLast 都是 task 代码，其中 doFirst 是往队列的前面插入代码，doLast 是往队列的后面插入代码

- task 的依赖

  可以使用 task taskA 的形式来指定依赖。指定依赖后，task 会在自己执行前先执行自己依赖的 task 

#### gradle 执行的什么周期

- 三个阶段
  - 初始化阶段
  - 定义阶段
  - 执行阶段
- 在阶段之间插入代码
  - 一二阶段之间
    - setting.gradle 的最后
  - 二三阶段之间



