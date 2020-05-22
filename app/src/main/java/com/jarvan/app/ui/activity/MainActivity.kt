package com.jarvan.app.ui.activity

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jarvan.app.R
import com.jarvan.lib_common.base.BaseActivity
import okio.Okio
import java.io.File

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        val filePath = externalCacheDir?.absolutePath + "/test_jarvan.txt"
        writeEnv(File(filePath))

        val liveString = MutableLiveData<String>()
        liveString.observe(this,
            Observer { Log.d("TAG", "called : s = $it") })
        liveString.postValue("程序")
    }

    private fun writeEnv(file: File) {
        Okio.sink(file).use { fileSink ->
            Okio.buffer(fileSink).use { bufferedSink ->
                for ((key, value) in System.getenv()) {
                    bufferedSink.writeUtf8(key)
                    bufferedSink.writeUtf8("=")
                    bufferedSink.writeUtf8(value)
                    bufferedSink.writeUtf8("\n")
                }
            }
        }
    }

}
