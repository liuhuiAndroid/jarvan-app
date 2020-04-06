package com.jarvan.app.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.google.gson.reflect.TypeToken
import com.jarvan.lib_network.HttpRepository
import com.jarvan.lib_network.data.Feed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

class HomeViewModel : AbsListViewModel<Feed>() {

    // 同步位标记，防止重复加载更多数据
    // AtomicBoolean 在多线程的环境下使用是线程安全的
    private val loadAfter = AtomicBoolean(false)

    override fun createDataSource(): DataSource<Int, Feed> {
        return FeedDataSource()
    }

    internal inner class FeedDataSource : ItemKeyedDataSource<Int, Feed>() {
        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Feed>
        ) {
            //加载初始化数据的
            loadData(0, params.requestedLoadSize, callback)
        }

        override fun loadAfter(
            params: LoadParams<Int>,
            callback: LoadCallback<Feed>
        ) {
            //向后加载分页数据的
            loadData(params.key, params.requestedLoadSize, callback)
        }

        override fun loadBefore(
            params: LoadParams<Int>,
            callback: LoadCallback<Feed>
        ) {
            //能够向前加载数据的
            callback.onResult(emptyList())
        }

        override fun getKey(item: Feed): Int {
            return item.id
        }
    }

    @SuppressLint("RestrictedApi")
    private fun loadData(
        key: Int,
        count: Int,
        callback: ItemKeyedDataSource.LoadCallback<Feed>
    ) {
        if (key > 0) {
            loadAfter.set(true)
        }
        // 可以先加载缓存，暂时不实现
        // =============================
        try {
            val call =
                HttpRepository.getApiService().getHotFeedsList("all", key, 1580651461, count)
            val response = call.execute().body()
            response?.let {
                it.data?.data?.let { it ->
                    callback.onResult(it)
                }
            }
            if (key > 0) {
                loadAfter.set(false)
            }
            Timber.i("loadData: key:$key")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("RestrictedApi")
    fun loadAfter(id: Int, callback: ItemKeyedDataSource.LoadCallback<Feed>) {
        if (loadAfter.get()) {
            callback.onResult(emptyList<Feed>())
            return
        }
        ArchTaskExecutor.getIOThreadExecutor()
            .execute { loadData(id, myPagingConfig.pageSize, callback) }
    }

}