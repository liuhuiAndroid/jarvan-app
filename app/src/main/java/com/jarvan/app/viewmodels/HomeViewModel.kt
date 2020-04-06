package com.jarvan.app.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.google.gson.reflect.TypeToken
import com.jarvan.lib_network.HttpRepository
import com.jarvan.lib_network.data.Feed
import timber.log.Timber
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
        request(
            execute = {
                val response =
                    HttpRepository.getApiService().getHotFeedsList("all", key, 0, count)
                val data = if (response.body == null) emptyList() else response.body
                callback.onResult(data as List<Feed>)
                if (key > 0) {
                    loadAfter.set(false)
                }
                Timber.i("loadData: key:$key")
            }
        )
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