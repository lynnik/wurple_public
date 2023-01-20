package com.wurple.presentation.flow.more.support.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemSupportBinding
import com.wurple.presentation.flow.base.adapter.BaseAdapter

class SupportAdapter(
    private val onItemClickListener: (item: SupportItem) -> Unit,
    private val onItemLongClickListener: (item: SupportItem) -> Unit
) :
    BaseAdapter<SupportItem, SupportViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSupportBinding.inflate(inflater, parent, false)
        return SupportViewHolder(binding, onItemClickListener, onItemLongClickListener)
    }

    private class DiffCallback : DiffUtil.ItemCallback<SupportItem>() {
        override fun areItemsTheSame(
            oldItem: SupportItem,
            newItem: SupportItem
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: SupportItem,
            newItem: SupportItem
        ): Boolean = oldItem == newItem
    }
}