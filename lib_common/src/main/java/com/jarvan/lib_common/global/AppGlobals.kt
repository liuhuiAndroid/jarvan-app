package com.jarvan.lib_common.global

import android.app.Application
import java.lang.reflect.InvocationTargetException

object AppGlobals {

    private var sApplication: Application? = null

    val application: Application?
        get() {
            if (sApplication == null) {
                try {
                    sApplication = Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication")
                        .invoke(null, null as Array<Any?>?) as Application
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }
            return sApplication
        }

}