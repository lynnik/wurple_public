package com.wurple.presentation.flow.profile.edit.experience.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemExperienceBinding
import com.wurple.domain.model.user.UserExperience
import com.wurple.presentation.flow.base.adapter.BaseAdapter

class EditProfileExperienceAdapter(private val onItemClickListener: (item: UserExperience) -> Unit) :
    BaseAdapter<UserExperience, EditProfileExperienceViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditProfileExperienceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemExperienceBinding.inflate(inflater, parent, false)
        return EditProfileExperienceViewHolder(binding, onItemClickListener)
    }

    private class DiffCallback : DiffUtil.ItemCallback<UserExperience>() {
        override fun areItemsTheSame(
            oldItem: UserExperience,
            newItem: UserExperience
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: UserExperience,
            newItem: UserExperience
        ): Boolean = oldItem == newItem
    }
}