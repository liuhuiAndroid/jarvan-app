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

open class HomeAdapter : PagedListAdapter<Feed, HomeAdapter.ViewHolder> {

    constructor() : super(object : DiffUtil.ItemCallback<Feed>() {
        override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return newItem.equals(oldItem)
        }
    })

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
}