package com.wurple.presentation.flow.profile.edit.education.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemEducationBinding
import com.wurple.domain.model.user.UserEducation
import com.wurple.presentation.flow.base.adapter.BaseAdapter

class EditProfileEducationAdapter(private val onItemClickListener: (item: UserEducation) -> Unit) :
    BaseAdapter<UserEducation, EditProfileEducationViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditProfileEducationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEducationBinding.inflate(inflater, parent, false)
        return EditProfileEducationViewHolder(binding, onItemClickListener)
    }

    private class DiffCallback : DiffUtil.ItemCallback<UserEducation>() {
        override fun areItemsTheSame(
            oldItem: UserEducation,
            newItem: UserEducation
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: UserEducation,
            newItem: UserEducation
        ): Boolean = oldItem == newItem
    }
}