package com.jarvan.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jarvan.app.R
import com.jarvan.app.databinding.LayoutRefreshViewBinding
import com.jarvan.app.viewmodels.AbsListViewModel
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import java.lang.reflect.ParameterizedType

open abstract class AbsListFragment<T, M : AbsListViewModel<T>>: Fragment(), OnLoadMoreListener,
    OnRefreshListener {

    private lateinit var layoutRefreshViewBinding: LayoutRefreshViewBinding

    private lateinit var pagedListAdapter: PagedListAdapter<T, RecyclerView.ViewHolder>

    lateinit var mViewModel: M

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutRefreshViewBinding = LayoutRefreshViewBinding.inflate(inflater, container, false)

        layoutRefreshViewBinding.refreshLayout.setEnableRefresh(true)
        layoutRefreshViewBinding.refreshLayout.setEnableLoadMore(true)
        layoutRefreshViewBinding.refreshLayout.setOnRefreshListener(this)
        layoutRefreshViewBinding.refreshLayout.setOnLoadMoreListener(this)

        pagedListAdapter = getAdapter()
        layoutRefreshViewBinding.recyclerView.adapter = pagedListAdapter
        val linearLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        layoutRefreshViewBinding.recyclerView.layoutManager = linearLayoutManager
        layoutRefreshViewBinding.recyclerView.itemAnimator = null

        //默认给列表中的Item 一个 10dp的ItemDecoration
        var decoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        getDrawable(requireContext(),R.drawable.list_divider)?.let {
            decoration.setDrawable(it)
        }
        layoutRefreshViewBinding.recyclerView.addItemDecoration(decoration)

        genericViewModel()
        return layoutRefreshViewBinding.root
    }

    open fun genericViewModel() {
        //利用 子类传递的 泛型参数实例化出 AbsListViewModel 对象。
        val type = javaClass.genericSuperclass as ParameterizedType
        val arguments = type.actualTypeArguments
        if (arguments.size > 1) {
            val argument = arguments[1]
            val modelClazz: Class<M> = (argument as Class<M>)
            mViewModel = ViewModelProvider(requireActivity())[modelClazz]
            //触发页面初始化数据加载的逻辑
            mViewModel.pageData.observe(viewLifecycleOwner, Observer {
                submitList(it)
            })

            //监听分页时有无更多数据,以决定是否关闭上拉加载的动画
            mViewModel.boundaryPageData
                .observe(viewLifecycleOwner, Observer {
                    finishRefresh(it)
                })
        }
    }

    abstract fun getAdapter(): PagedListAdapter<T, RecyclerView.ViewHolder>

    open fun submitList(result: PagedList<T>) {
        // 只有当新数据集合大于0 的时候，才调用adapter.submitList
        // 否则可能会出现 页面----有数据----->被清空-----空布局
        if (result.size > 0) {
            pagedListAdapter.submitList(result)
        }
        finishRefresh(result.size > 0)
    }

    open fun finishRefresh(hasData: Boolean) {
        var hasData = hasData
        val currentList: PagedList<T>? = pagedListAdapter.currentList
        hasData = hasData || currentList != null && currentList.size > 0
        val state: RefreshState = layoutRefreshViewBinding.refreshLayout.getState()
        if (state.isFooter && state.isOpening) {
            layoutRefreshViewBinding.refreshLayout.finishLoadMore()
        } else if (state.isHeader && state.isOpening) {
            layoutRefreshViewBinding.refreshLayout.finishRefresh()
        }
        if (hasData) {
            // mEmptyView.setVisibility(View.GONE)
        } else {
            // mEmptyView.setVisibility(View.VISIBLE)
        }
    }
}