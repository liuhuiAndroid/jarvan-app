package com.jarvan.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

open abstract class AbsListViewModel<T> : ViewModel() {

    private val myPagingConfig = Config(
        pageSize = 10,
        initialLoadSizeHint = 12,
        enablePlaceholders = false
    )

    private lateinit var dataSource: DataSource<Int, T>

    private val boundaryPageData = MutableLiveData<Boolean>()

    private var factory: DataSource.Factory<Int, T> =
        object : DataSource.Factory<Int, T>() {
            override fun create(): DataSource<Int, T> {
                if (dataSource == null) {
                    dataSource = createDataSource()
                } else if (dataSource.isInvalid) {
                    dataSource = createDataSource()
                }
                return dataSource
            }
        }

    abstract fun createDataSource(): DataSource<Int, T>

    // PagedList数据被加载的边界回调Callback
    // 但不是每一次分页都会回调这里，具体请看ContiguousPagedList#mReceiver#onPageResult
    // deferBoundaryCallbacks
    private var callback: PagedList.BoundaryCallback<T> = object : PagedList.BoundaryCallback<T>() {
        override fun onZeroItemsLoaded() {
            // 新提交的PagedList中没有数据
            boundaryPageData.postValue(false)
        }

        override fun onItemAtFrontLoaded(itemAtFront: T) {
            // 新提交的PagedList中第一条数据被加载到列表上
            boundaryPageData.postValue(true)
        }

        override fun onItemAtEndLoaded(itemAtEnd: T) {
            // 新提交的PagedList中最后一条数据被加载到列表上
        }
    }

    var pageData: LiveData<PagedList<T>>? = LivePagedListBuilder(factory, myPagingConfig)
        .setInitialLoadKey(0)
        .setBoundaryCallback(callback)
        .build()
}