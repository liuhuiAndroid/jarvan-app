package com.jarvan.app.ui

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import java.util.*

class MutablePageKeyedDataSource<Value> : PageKeyedDataSource<Int, Value>() {

    var data: MutableList<Value> = ArrayList()

    @SuppressLint("RestrictedApi")
    fun buildNewPagedList(config: PagedList.Config?): PagedList<Value> {
        return PagedList.Builder<Int, Value>(this, config!!)
            .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
            .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
            .build()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int?, Value>
    ) {
        callback.onResult(data, null, null)
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Value>
    ) {
        callback.onResult(emptyList(), null)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Value>
    ) {
        callback.onResult(emptyList(), null)
    }

}