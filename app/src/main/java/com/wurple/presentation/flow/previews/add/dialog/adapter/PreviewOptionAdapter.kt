package com.wurple.presentation.flow.previews.add.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemPreviewOptionBinding
import com.wurple.domain.model.preview.PreviewVisibilityValue
import com.wurple.presentation.flow.base.adapter.BaseAdapter
import com.wurple.presentation.flow.previews.add.dialog.adapter.viewholder.PreviewOptionViewHolder

class PreviewOptionAdapter(
    private val onItemClickListener: (previewVisibilityValue: PreviewVisibilityValue) -> Unit
) : BaseAdapter<PreviewOptionItem, PreviewOptionViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewOptionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPreviewOptionBinding.inflate(inflater, parent, false)
        return PreviewOptionViewHolder(binding, onItemClickListener)
    }

    private class DiffCallback : DiffUtil.ItemCallback<PreviewOptionItem>() {
        override fun areItemsTheSame(
            oldItem: PreviewOptionItem,
            newItem: PreviewOptionItem
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: PreviewOptionItem,
            newItem: PreviewOptionItem
        ): Boolean = oldItem == newItem
    }
}