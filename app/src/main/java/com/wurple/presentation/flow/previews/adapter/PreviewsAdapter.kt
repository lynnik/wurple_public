package com.wurple.presentation.flow.previews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemPreviewBinding
import com.wurple.presentation.flow.base.adapter.BaseAdapter
import com.wurple.presentation.model.preview.PreviewListItem

class PreviewsAdapter(
    private val onEditClickListener: (item: PreviewListItem) -> Unit,
    private val onShareClickListener: (item: PreviewListItem) -> Unit,
    private val onItemClickListener: (item: PreviewListItem) -> Unit
) :
    BaseAdapter<PreviewListItem, PreviewsViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreviewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPreviewBinding.inflate(inflater, parent, false)
        return PreviewsViewHolder(
            binding = binding,
            onEditClickListener = onEditClickListener,
            onShareClickListener = onShareClickListener,
            onItemClickListener = onItemClickListener
        )
    }

    private class DiffCallback : DiffUtil.ItemCallback<PreviewListItem>() {
        override fun areItemsTheSame(
            oldItem: PreviewListItem,
            newItem: PreviewListItem
        ): Boolean = oldItem.previewId == newItem.previewId

        override fun areContentsTheSame(
            oldItem: PreviewListItem,
            newItem: PreviewListItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}
