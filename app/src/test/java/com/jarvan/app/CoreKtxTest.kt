package com.jarvan.app

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.view.doOnPreDraw
import org.junit.Test

class CoreKtxTest {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var view: View

    private val value = true

    @Test
    fun playWithStandard() {
        val myUriString = "https://www.baidu.com"
        // Kotlin
        val uri1 = Uri.parse(myUriString)
        // Kotlin with Android KTX
        val uri2 = myUriString.toUri()

        // Kotlin
        sharedPreferences
            .edit()  // create an Editor
            .putBoolean("key", value)
            .apply() // write to disk asynchronously
        // Kotlin with Android KTX
        sharedPreferences.edit {
            putBoolean("key", value)
        }

        // Kotlin
        view.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    view.viewTreeObserver.removeOnPreDrawListener(this)
                    actionToBeTriggered()
                    return true
                }
            }
        )
        // Kotlin with Android KTX
        view.doOnPreDraw {
            actionToBeTriggered()
        }
    }

    private fun actionToBeTriggered() {

    }

}
