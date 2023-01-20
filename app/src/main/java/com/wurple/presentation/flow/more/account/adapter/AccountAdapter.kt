package com.wurple.presentation.flow.more.account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemAccountBinding
import com.wurple.presentation.flow.base.adapter.BaseAdapter

class AccountAdapter(private val onItemClickListener: (item: AccountItem) -> Unit) :
    BaseAdapter<AccountItem, AccountViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(inflater, parent, false)
        return AccountViewHolder(binding, onItemClickListener)
    }

    private class DiffCallback : DiffUtil.ItemCallback<AccountItem>() {
        override fun areItemsTheSame(
            oldItem: AccountItem,
            newItem: AccountItem
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: AccountItem,
            newItem: AccountItem
        ): Boolean = oldItem == newItem
    }
}