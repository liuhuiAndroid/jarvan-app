package com.jarvan.app.ui

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import java.util.*

/**
 * 一个可变更的ItemKeyedDataSource 数据源
 *
 * 工作原理是：我们知道DataSource是会被PagedList 持有的。
 * 一旦，我们调用了new PagedList.Builder<Key, Value>().build(); 那么就会立刻触发当前DataSource的loadInitial()方法，而且是同步
 * 详情见ContiguousPagedList的构造函数,而我们在当前DataSource的loadInitial()方法中返回了 最新的数据集合 data。
 * 一旦，我们再次调用PagedListAdapter#submitList()方法 就会触发差分异计算 把新数据变更到列表之上了。
 *
 * @param <Key>
 * @param <Value>
 */
abstract class MutableItemKeyedDataSource<Key, Value> : ItemKeyedDataSource<Key, Value>() {

    private var mDataSource: ItemKeyedDataSource<Key, Value>? = null

    open fun MutableItemKeyedDataSource(dataSource: ItemKeyedDataSource<Key, Value>) {
        mDataSource = dataSource
    }

    var data: MutableList<Value> = ArrayList()

    @SuppressLint("RestrictedApi")
    fun buildNewPagedList(config: PagedList.Config): PagedList<Value> {
        return PagedList.Builder<Key, Value>(this, config)
            .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
            .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
            .build()
    }

    override fun loadInitial(params: LoadInitialParams<Key>, callback: LoadInitialCallback<Value>) {
        callback.onResult(data)
    }

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Value>) {
        callback.onResult(emptyList())
    }

    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Value>) {
        mDataSource?.loadAfter(params, callback)
    }

    abstract override fun getKey(item: Value): Key

}