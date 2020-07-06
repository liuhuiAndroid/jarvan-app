package com.jarvan.app

import android.R
import android.app.Application
import android.content.Context
import android.os.Environment
import com.alibaba.android.arouter.launcher.ARouter
import com.jarvan.app.utilities.DynamicTimeFormat
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import okio.Okio
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber
import java.io.File

/**
 * ajin233333
 */
class JarvanApplication : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        // ARouter初始化
        ARouter.init(this)

        Timber.plant(Timber.DebugTree())
    }

    override val kodein by Kodein.lazy {
        bind<Context>() with singleton { this@JarvanApplication }
        import(androidXModule(this@JarvanApplication))
    }

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> ClassicsHeader(context) }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) }
    }

}