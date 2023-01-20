package com.wurple.presentation.flow.profile.edit.languages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemLanguageBinding
import com.wurple.domain.model.user.UserLanguage
import com.wurple.presentation.flow.base.adapter.BaseAdapter

class EditProfileLanguageAdapter(private val onItemClickListener: (item: UserLanguage) -> Unit) :
    BaseAdapter<UserLanguage, EditProfileLanguageViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditProfileLanguageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLanguageBinding.inflate(inflater, parent, false)
        return EditProfileLanguageViewHolder(binding, onItemClickListener)
    }

    private class DiffCallback : DiffUtil.ItemCallback<UserLanguage>() {
        override fun areItemsTheSame(
            oldItem: UserLanguage,
            newItem: UserLanguage
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: UserLanguage,
            newItem: UserLanguage
        ): Boolean = oldItem == newItem
    }
}