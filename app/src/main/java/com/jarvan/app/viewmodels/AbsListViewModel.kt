package com.jarvan.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

open abstract class AbsListViewModel<T> : BaseViewModel() {

    val myPagingConfig = Config(
        pageSize = 8,
        initialLoadSizeHint = 8,
        enablePlaceholders = false
    )

    // 数据源
    var dataSource: DataSource<Int, T>? = null

    // PagedList：一个继承了 AbstractList 的 List 子类， 包括了数据源获取的数据
    var pageData: LiveData<PagedList<T>>

    val boundaryPageData = MutableLiveData<Boolean>()

    init {
        // DataSource中的数据加载到边界时的回调Callback
        // 但不是每一次分页都会回调这里，具体请看ContiguousPagedList#mReceiver#onPageResult
        // deferBoundaryCallbacks
        var callback: PagedList.BoundaryCallback<T> = object : PagedList.BoundaryCallback<T>() {
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

        var factory: DataSource.Factory<Int, T> =
            object : DataSource.Factory<Int, T>() {
                override fun create(): DataSource<Int, T> {
                    dataSource = createDataSource()
                    return dataSource!!
                }
            }

        // LivePagedListBuilder：将PagedList和LiveData整合成LiveData<PagedList>
        // LivePageListBuilder 此类是从 DataSource.Factory 构建 LiveData<PagedList>
        // 用LivePagedListBuilder类构建LiveData类：让数据和Activity或Fragment的生命周期绑定
        pageData = LivePagedListBuilder(factory, myPagingConfig)
            .setInitialLoadKey(0)
            .setBoundaryCallback(callback)
            .build()
    }

    abstract fun createDataSource(): DataSource<Int, T>

}