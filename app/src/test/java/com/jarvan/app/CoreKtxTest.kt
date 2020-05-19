package com.jarvan.app

import android.net.Uri
import androidx.core.net.toUri
import org.junit.Test

class CoreKtxTest {

    @Test
    fun playWithStandard() {
        val myUriString = "https://www.baidu.com"
        // Kotlin
        val uri1 = Uri.parse(myUriString)
        // Kotlin with Android KTX
        val uri2 = myUriString.toUri()
    }

}
