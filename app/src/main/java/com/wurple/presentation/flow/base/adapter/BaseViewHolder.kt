package com.wurple.presentation.flow.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<ITEM>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(item: ITEM)
}