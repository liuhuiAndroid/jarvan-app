package com.jarvan.app.ui.fragment

import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jarvan.app.adapters.HomeAdapter
import com.jarvan.app.ui.AbsListFragment
import com.jarvan.app.viewmodels.HomeViewModel
import com.jarvan.lib_network.data.Feed
import com.scwang.smartrefresh.layout.api.RefreshLayout

class HomeFragment : AbsListFragment<Feed, HomeViewModel>() {

    /**
     * 上拉分页，paging帮忙处理，但是如果数据返回为空，不会再次分页，需要手动加载
     */
    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        //invalidate 之后Paging会重新创建一个DataSource 重新调用它的loadInitial方法加载初始化数据
        //详情见：LivePagedListBuilder#compute方法
        mViewModel.dataSource?.invalidate()
    }

    override fun getAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {
        return object : HomeAdapter() {
            override fun onCurrentListChanged(
                previousList: PagedList<Feed>?,
                currentList: PagedList<Feed>?
            ) {
                //这个方法是在我们每提交一次 pagelist对象到adapter 就会触发一次
                //每调用一次 adpater.submitlist
                if (previousList != null && currentList != null) {
                    if (!currentList.containsAll(previousList)) {
                        layoutRefreshViewBinding.recyclerView.scrollToPosition(0)
                    }
                }
            }
        } as PagedListAdapter<Feed, RecyclerView.ViewHolder>
    }

}
