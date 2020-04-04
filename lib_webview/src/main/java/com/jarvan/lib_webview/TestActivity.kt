package com.jarvan.lib_webview

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/webview/test_activity")
class TestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "i am test.", Toast.LENGTH_LONG).show()
    }

}