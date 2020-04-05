package com.jarvan.app.core.ext

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/*
    扩展点击事件，参数为方法
 */
fun View.onClick(method: () -> Unit): View {
    setOnClickListener { method() }
    return this
}

/*
    扩展点击事件
 */
fun View.onClick(listener: View.OnClickListener): View {
    setOnClickListener(listener)
    return this
}

/*
    设置圆角
 */
fun View.setRoundRect(): View {
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, 20f)
        }
    }
    this.clipToOutline = true
    return this
}

/*
    设置圆角
 */
fun View.setRoundRect(radius: Float): View {
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
    this.clipToOutline = true
    return this
}
