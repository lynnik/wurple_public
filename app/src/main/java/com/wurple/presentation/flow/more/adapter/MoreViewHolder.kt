package com.wurple.presentation.flow.more.adapter

import com.wurple.databinding.ItemMoreBinding
import com.wurple.presentation.flow.base.adapter.BaseViewHolder

class MoreViewHolder(
    private val binding: ItemMoreBinding,
    private val onItemClickListener: (item: MoreItem) -> Unit
) : BaseViewHolder<MoreItem>(binding.root) {
    override fun onBind(item: MoreItem) {
        with(binding) {
            imageView.setImageResource(item.iconResId)
            textView.text = itemView.context.getString(item.titleResId)
            container.setOnClickListener { onItemClickListener(item) }
        }
    }
}