package com.wurple.presentation.flow.base.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<ITEM, VH : BaseViewHolder<ITEM>>(
    diffCallback: DiffUtil.ItemCallback<ITEM>
) : ListAdapter<ITEM, VH>(diffCallback) {
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}
