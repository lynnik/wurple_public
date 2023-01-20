package com.wurple.presentation.flow.profile.edit.experience.adapter

import com.wurple.R
import com.wurple.databinding.ItemExperienceBinding
import com.wurple.domain.model.user.UserExperience
import com.wurple.presentation.flow.base.adapter.BaseViewHolder

class EditProfileExperienceViewHolder(
    private val binding: ItemExperienceBinding,
    private val onItemClickListener: (item: UserExperience) -> Unit
) : BaseViewHolder<UserExperience>(binding.root) {
    override fun onBind(item: UserExperience) {
        with(binding) {
            textView.text = item.position
            subtextView.text = item.companyName
            dateTextView.text = item.fromToFormattedDateRange
                ?.let { range ->
                    itemView.context.getString(
                        R.string.experience_date_range,
                        item.fromDate.formattedDate,
                        if (item.isCurrent) {
                            itemView.context.getString(R.string.common_date_present)
                        } else {
                            item.toDate.formattedDate
                        },
                        range
                    )
                }
                ?: run {
                    itemView.context.getString(
                        R.string.experience_date,
                        item.fromDate.formattedDate,
                        if (item.isCurrent) {
                            itemView.context.getString(R.string.common_date_present)
                        } else {
                            item.toDate.formattedDate
                        }
                    )
                }
            container.setOnClickListener { onItemClickListener(item) }
        }
    }
}