package com.jarvan.app.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.jarvan.app.R
import com.jarvan.lib_common.base.BaseActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.withPermissionsCheck
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_splash)

        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }

        Observable.timer(MIN_WAIT_TIME.toLong(), TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                jump()
            }
    }

    private fun jump() = withPermissionsCheck(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        onShowRationale = ::onPermissionShowRationale,
        onPermissionDenied = ::onPermissionDenied,
        onNeverAskAgain = ::onPermissionNeverAskAgain
    ) {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    private fun onPermissionDenied() {
        Toast.makeText(this, "无法正常获取权限：onCameraDenied", Toast.LENGTH_SHORT).show()
    }

    private fun onPermissionShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    private fun onPermissionNeverAskAgain() {
        Toast.makeText(this, "无法正常获取权限：onPermissionNeverAskAgain", Toast.LENGTH_SHORT).show()
    }

    companion object {
        // 应用程序在闪屏界面最短的停留时间。
        const val MIN_WAIT_TIME = 2000
        // 应用程序在闪屏界面最长的停留时间。
        const val MAX_WAIT_TIME = 5000
    }

}