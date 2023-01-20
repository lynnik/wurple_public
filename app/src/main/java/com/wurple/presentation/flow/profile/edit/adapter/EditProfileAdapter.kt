package com.wurple.presentation.flow.profile.edit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemEditProfileImageBinding
import com.wurple.databinding.ItemEditProfileTextBinding
import com.wurple.presentation.flow.base.adapter.BaseAdapter
import com.wurple.presentation.flow.profile.edit.adapter.viewholder.EditProfileImageViewHolder
import com.wurple.presentation.flow.profile.edit.adapter.viewholder.EditProfileTextViewHolder
import com.wurple.presentation.flow.profile.edit.adapter.viewholder.EditProfileViewHolder

class EditProfileAdapter(private val onItemClickListener: (item: EditProfileItem) -> Unit) :
    BaseAdapter<EditProfileItem, EditProfileViewHolder>(DiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EditProfileItem.Image -> IMAGE_VIEW_TYPE
            else -> TEXT_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditProfileViewHolder {
        return when (viewType) {
            IMAGE_VIEW_TYPE -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemEditProfileImageBinding.inflate(inflater, parent, false)
                EditProfileImageViewHolder(binding, onItemClickListener)
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemEditProfileTextBinding.inflate(inflater, parent, false)
                EditProfileTextViewHolder(binding, onItemClickListener)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<EditProfileItem>() {
        override fun areItemsTheSame(
            oldItem: EditProfileItem,
            newItem: EditProfileItem
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: EditProfileItem,
            newItem: EditProfileItem
        ): Boolean = oldItem == newItem
    }

    companion object {
        private const val IMAGE_VIEW_TYPE = 0
        private const val TEXT_VIEW_TYPE = 1
    }
}