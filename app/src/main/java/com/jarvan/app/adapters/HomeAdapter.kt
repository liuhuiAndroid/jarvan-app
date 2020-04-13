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

open class HomeAdapter : PagedListAdapter<Feed, HomeAdapter.ViewHolder>(diffCallback) {

    class ViewHolder(itemView: View, private var mBinding: LayoutHomeItemBinding) :
        RecyclerView.ViewHolder(itemView) {

        fun bindData(item: Feed) {
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
        feed?.let {
            holder.bindData(it)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return newItem == oldItem
            }
        }
    }
}