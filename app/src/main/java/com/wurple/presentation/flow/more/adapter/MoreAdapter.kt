package com.wurple.presentation.flow.more.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemMoreBinding
import com.wurple.presentation.flow.base.adapter.BaseAdapter

class MoreAdapter(private val onItemClickListener: (item: MoreItem) -> Unit) :
    BaseAdapter<MoreItem, MoreViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMoreBinding.inflate(inflater, parent, false)
        return MoreViewHolder(binding, onItemClickListener)
    }

    private class DiffCallback : DiffUtil.ItemCallback<MoreItem>() {
        override fun areItemsTheSame(
            oldItem: MoreItem,
            newItem: MoreItem
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: MoreItem,
            newItem: MoreItem
        ): Boolean = oldItem == newItem
    }
}