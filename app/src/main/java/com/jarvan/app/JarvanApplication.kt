package com.jarvan.app

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

class JarvanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // ARouter初始化
        ARouter.init(this)
    }

}