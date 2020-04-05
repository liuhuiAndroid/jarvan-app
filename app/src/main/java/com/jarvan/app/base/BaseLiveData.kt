package com.jarvan.app.base

import android.os.Looper
import androidx.lifecycle.MutableLiveData

class BaseLiveData<T: Any?> : MutableLiveData<T?>() {

    open fun update(value: T?) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.setValue(value)
        } else {
            postValue(value)
        }
    }

}