package com.wurple.presentation.flow.more.account.adapter

import com.wurple.databinding.ItemAccountBinding
import com.wurple.presentation.flow.base.adapter.BaseViewHolder

class AccountViewHolder(
    private val binding: ItemAccountBinding,
    private val onItemClickListener: (item: AccountItem) -> Unit
) : BaseViewHolder<AccountItem>(binding.root) {
    override fun onBind(item: AccountItem) {
        with(binding) {
            imageView.setImageResource(item.iconResId)
            textView.text = itemView.context.getString(item.titleResId)
            container.setOnClickListener { onItemClickListener(item) }
        }
    }
}