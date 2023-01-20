package com.wurple.presentation.flow.profile.edit.adapter.viewholder

import coil.load
import com.wurple.R
import com.wurple.databinding.ItemEditProfileImageBinding
import com.wurple.presentation.flow.profile.edit.adapter.EditProfileItem

class EditProfileImageViewHolder(
    private val binding: ItemEditProfileImageBinding,
    private val onItemClickListener: (item: EditProfileItem) -> Unit
) : EditProfileViewHolder(binding.root) {
    override fun onBind(item: EditProfileItem) {
        with(binding) {
            val editProfileItemImage = item as EditProfileItem.Image
            userImageImageView.load(editProfileItemImage.imageUrl) {
                error(R.drawable.image_user)
            }
            container.setOnClickListener { onItemClickListener(item) }
        }
    }
}