package com.jarvan.lib_network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun ViewModel.launch(
    block: suspend CoroutineScope.() -> Unit,
    onError: (error: Throwable) -> Unit = {}
) {
    // 统一异常处理
    fun errorHandler(onError: (error: Throwable) -> Unit = {}): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, error -> onError(error) }
    }

    viewModelScope.launch(errorHandler(onError)) {
        block.invoke(this)
    }
}
