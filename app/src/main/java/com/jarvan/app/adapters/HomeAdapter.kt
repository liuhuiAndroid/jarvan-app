package com.jarvan.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jarvan.app.R
import com.jarvan.app.databinding.LayoutHomeItemBinding
import com.jarvan.lib_network.data.Feed

/**
 * https://www.jianshu.com/p/7992060cc2cb
 * 不需要新增任何增、删、改方法，只需要使用 adapter.submitList(List) 方法
 */
open class HomeAdapter : PagedListAdapter<Feed, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(itemView: View, private var mBinding: LayoutHomeItemBinding) :
        RecyclerView.ViewHolder(itemView) {

        fun bindData(item: Feed?) {
            mBinding.feed = item
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.layout_home_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // attachToRoot:
        // 如果attachToRoot=true, 则布局文件将转化为View并绑定到root，然后返回root作为根节点的整个View;
        // 如果attachToRoot=false,则布局文件转化为View但不绑定到root，返回以布局文件根节点为根节点的View。
        val binding =
            LayoutHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feed = getItem(position)
        holder.bindData(feed)
    }

    companion object {
        // 处理Adapter的更新：比较两个数据集，用newList和oldList进行比较，得出最小的变化量。
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Feed>() {
            // 两个对象是否是同一个对象
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed) = oldItem.id == newItem.id
            // 两个对象的内容是否一致，如果不一致，那么 它就将对列表进行重绘和动画加载，反之将不做任何的操作。
            override fun areContentsTheSame(oldItem: Feed, newItem: Feed) = newItem == oldItem
        }
    }
}