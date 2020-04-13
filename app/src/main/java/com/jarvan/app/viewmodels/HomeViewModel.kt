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

/**
 * Paging:https://www.jianshu.com/p/10bf4bf59122
 */
class HomeViewModel : AbsListViewModel<Feed>() {

    // 同步位标记，防止重复加载更多数据
    // AtomicBoolean 是 java.util.concurrent.atomic 的原子变量的类
    // AtomicBoolean 在多线程的环境下使用是线程安全的
    private val loadAfter = AtomicBoolean(false)

    override fun createDataSource(): DataSource<Int, Feed> {
        return FeedDataSource()
    }

    /**
     * 三种DataSource：
     * PageKeyedDataSource：适用于目标数据根据页信息请求数据的场景
     * ItemKeyedDataSource：适用于目标数据的加载依赖特定item的信息
     * PositionalDataSource：适用于目标数据总数固定，通过特定的位置加载数据
     */
    internal inner class FeedDataSource : ItemKeyedDataSource<Int, Feed>() {
        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Feed>
        ) {
            // 加载初始化数据的
            // requestedLoadSize：Requested number of items to load.
            loadData(0, params.requestedLoadSize, callback)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            // 向后加载分页数据的
            loadData(params.key, params.requestedLoadSize, callback)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            // 能够向前加载数据的
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

    // 屏蔽一切新Api中才能使用的方法报的Android Lint错误
    @SuppressLint("RestrictedApi")
    fun loadAfter(id: Int, callback: ItemKeyedDataSource.LoadCallback<Feed>) {
        if (loadAfter.get()) {
            callback.onResult(emptyList<Feed>())
            return
        }
        // TaskExecutor：在主线程/IO线程执行任务
        // ArchTaskExecutor：代理模式，
        // DefaultTaskExecutor：ArchTaskExecutor的默认代理类，当client没有手动设置delegate时，使用其执行任务。
        ArchTaskExecutor.getIOThreadExecutor()
            .execute { loadData(id, myPagingConfig.pageSize, callback) }
    }

}