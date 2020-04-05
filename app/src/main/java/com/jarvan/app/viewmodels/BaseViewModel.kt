package com.jarvan.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel : ViewModel() {

    fun <T> request(
        onError: (error: Throwable) -> Unit = {},
        execute: suspend CoroutineScope.() -> T
    ) {
        viewModelScope.launch(errorHandler { onError.invoke(it) }) {
            launch(Dispatchers.IO) {
                execute()
            }
        }
    }

    private fun errorHandler(onError: (error: Throwable) -> Unit): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Timber.d(throwable)
            onError.invoke(throwable)
        }
    }

}
