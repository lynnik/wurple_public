package com.wurple.presentation.flow.profile.edit.languages.adapter

import com.wurple.databinding.ItemLanguageBinding
import com.wurple.domain.model.user.UserLanguage
import com.wurple.presentation.flow.base.adapter.BaseViewHolder

class EditProfileLanguageViewHolder(
    private val binding: ItemLanguageBinding,
    private val onItemClickListener: (item: UserLanguage) -> Unit
) : BaseViewHolder<UserLanguage>(binding.root) {
    override fun onBind(item: UserLanguage) {
        with(binding) {
            textView.text = item.language.name
            subtextView.text = item.languageLevel.name
            container.setOnClickListener { onItemClickListener(item) }
        }
    }
}