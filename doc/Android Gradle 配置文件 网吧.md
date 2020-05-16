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

  - android

    - buildTypes

    - productFlavors 渠道包

    - flavorDimensions 渠道包

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

- setting.gradle

- gradle-wrapper

- gradle 语法 

#### 闭包

- 相当于可以传递的代码块

#### buildType 和 produceFlavors

 

#### compile，implementation 和 api

- implementation：不会传递依赖
- compile / api：会传递依赖；api 是 compile 的替代品，效果完全相同
- 当依赖被传递时，二级依赖的改动会导致0级项目重新编译；当依赖不传递时，二级依赖的改动不会导致0级项目重新编译

#### 项目结构

- 单 project
- 多 project

#### task

- 使用方法
- task 的结构
- doFirst doLast 和普通代码段的区别
- task 的依赖

#### gradle 执行的什么周期

- 三个阶段
  - 初始化阶段
  - 定义阶段
  - 执行阶段
- 在阶段之间插入代码
  - 一二阶段之间
    - setting.gradle 的最后
  - 二三阶段之间



