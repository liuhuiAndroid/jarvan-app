package com.jarvan.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jarvan.app.adapters.HomeAdapter
import com.jarvan.lib_network.data.Feed
import com.jarvan.app.databinding.FragmentHomeBinding
import com.jarvan.app.ui.AbsListFragment
import com.jarvan.app.viewmodels.HomeViewModel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

class HomeFragment : AbsListFragment<Feed, HomeViewModel>() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    /**
     * 上拉分页，paging帮忙处理，但是如果数据返回为空，不会再次分页，需要手动加载
     */
    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        //invalidate 之后Paging会重新创建一个DataSource 重新调用它的loadInitial方法加载初始化数据
        //详情见：LivePagedListBuilder#compute方法
        mViewModel.dataSource.invalidate()
    }

    override fun getAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {
        return object : HomeAdapter() {

        } as PagedListAdapter<Feed, RecyclerView.ViewHolder>
    }

}
