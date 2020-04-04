package com.jarvan.lib_common.utilities

import android.content.Context

object PixUtils {

    fun dp2px(context: Context, dpValue: Int): Int {
        val metrics = context.resources.displayMetrics
        return (metrics.density * dpValue + 0.5f).toInt()
    }

}