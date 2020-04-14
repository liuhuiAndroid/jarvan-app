package com.jarvan.lib_common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jarvan.lib_common.utilities.StatusBarUtil

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 实现沉浸式效果
        StatusBarUtil.fitSystemBar(this)
    }

}