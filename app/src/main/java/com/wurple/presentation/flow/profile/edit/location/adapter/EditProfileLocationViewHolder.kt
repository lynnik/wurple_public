package com.wurple.presentation.flow.profile.edit.location.adapter

import com.wurple.databinding.ItemLocationBinding
import com.wurple.domain.model.location.LocationSearch
import com.wurple.presentation.flow.base.adapter.BaseViewHolder

class EditProfileLocationViewHolder(
    private val binding: ItemLocationBinding,
    private val onItemClickListener: (item: LocationSearch) -> Unit
) : BaseViewHolder<LocationSearch>(binding.root) {
    override fun onBind(item: LocationSearch) {
        with(binding) {
            textView.text = item.primaryText
            subtextView.text = item.secondaryText
            container.setOnClickListener { onItemClickListener(item) }
        }
    }
}