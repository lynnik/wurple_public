package com.wurple.presentation.flow.more.support.adapter

import com.wurple.databinding.ItemSupportBinding
import com.wurple.presentation.flow.base.adapter.BaseViewHolder

class SupportViewHolder(
    private val binding: ItemSupportBinding,
    private val onItemClickListener: (item: SupportItem) -> Unit,
    private val onItemLongClickListener: (item: SupportItem) -> Unit
) : BaseViewHolder<SupportItem>(binding.root) {
    override fun onBind(item: SupportItem) {
        with(binding) {
            imageView.setImageResource(item.iconResId)
            textView.text = itemView.context.getString(item.titleResId)
            container.setOnClickListener { onItemClickListener(item) }
            container.setOnLongClickListener {
                onItemLongClickListener(item)
                true
            }
        }
    }
}