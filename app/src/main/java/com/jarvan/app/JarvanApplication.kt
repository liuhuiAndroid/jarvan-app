package com.jarvan.app

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class JarvanApplication : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        // ARouter初始化
        ARouter.init(this)
    }

    override val kodein by Kodein.lazy {
        bind<Context>() with singleton { this@JarvanApplication }
        import(androidXModule(this@JarvanApplication))
    }

}