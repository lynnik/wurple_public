package com.wurple.presentation.flow.profile.edit.education.adapter

import com.wurple.databinding.ItemEducationBinding
import com.wurple.domain.model.user.UserEducation
import com.wurple.presentation.flow.base.adapter.BaseViewHolder

class EditProfileEducationViewHolder(
    private val binding: ItemEducationBinding,
    private val onItemClickListener: (item: UserEducation) -> Unit
) : BaseViewHolder<UserEducation>(binding.root) {
    override fun onBind(item: UserEducation) {
        with(binding) {
            textView.text = item.degree
            subtextView.text = item.institution
            dateTextView.text = item.graduationDate.formattedDate
            container.setOnClickListener { onItemClickListener(item) }
        }
    }
}