package com.wurple.presentation.flow.profile.edit.adapter.viewholder

import com.wurple.R
import com.wurple.databinding.ItemEditProfileTextBinding
import com.wurple.domain.extension.empty
import com.wurple.presentation.extension.gone
import com.wurple.presentation.extension.visible
import com.wurple.presentation.flow.profile.edit.adapter.EditProfileItem

class EditProfileTextViewHolder(
    private val binding: ItemEditProfileTextBinding,
    private val onItemClickListener: (item: EditProfileItem) -> Unit
) : EditProfileViewHolder(binding.root) {
    override fun onBind(item: EditProfileItem) {
        val context = itemView.context
        with(binding) {
            when (item) {
                is EditProfileItem.UserFullName -> {
                    captionTextView.visible()
                    captionTextView.text = context.getString(R.string.edit_profile_full_name)
                    textTextView.text = item.text.ifEmpty {
                        context.getString(
                            R.string.common_user_full_name,
                            context.getString(R.string.edit_profile_name_first_name_stub),
                            context.getString(R.string.edit_profile_name_last_name_stub)
                        )
                    }
                }
                is EditProfileItem.UserDob -> {
                    captionTextView.visible()
                    captionTextView.text = context.getString(R.string.edit_profile_dob)
                    textTextView.text = item.text.ifEmpty {
                        context.getString(R.string.edit_profile_dob_stub)
                    }
                }
                is EditProfileItem.UserLocation -> {
                    captionTextView.visible()
                    captionTextView.text = context.getString(R.string.edit_profile_location)
                    textTextView.text = item.text.ifEmpty {
                        context.getString(R.string.edit_profile_location_stub)
                    }
                }
                is EditProfileItem.UserEmail -> {
                    captionTextView.visible()
                    captionTextView.text = context.getString(R.string.edit_profile_email)
                    textTextView.text = item.text
                }
                is EditProfileItem.UserUsername -> {
                    captionTextView.visible()
                    captionTextView.text = context.getString(R.string.edit_profile_username)
                    textTextView.text = if (item.text.isEmpty()) {
                        context.getString(R.string.edit_profile_username_stub)
                    } else {
                        context.getString(R.string.common_username_with_prefix, item.text)
                    }
                }
                is EditProfileItem.UserBio -> {
                    captionTextView.visible()
                    captionTextView.text = context.getString(R.string.edit_profile_bio)
                    textTextView.text = item.text.ifEmpty {
                        context.getString(R.string.edit_profile_bio_stub)
                    }
                }
                EditProfileItem.UserSkills -> {
                    captionTextView.gone()
                    captionTextView.text = String.empty
                    textTextView.text = context.getString(R.string.skills_title)
                }
                EditProfileItem.UserExperience -> {
                    captionTextView.gone()
                    captionTextView.text = String.empty
                    textTextView.text = context.getString(R.string.experience_title)
                }
                EditProfileItem.UserEducation -> {
                    captionTextView.gone()
                    captionTextView.text = String.empty
                    textTextView.text = context.getString(R.string.education_title)
                }
                EditProfileItem.UserLanguages -> {
                    captionTextView.gone()
                    captionTextView.text = String.empty
                    textTextView.text = context.getString(R.string.languages_title)
                }
                else -> Unit
            }
            container.setOnClickListener { onItemClickListener(item) }
        }
    }
}
