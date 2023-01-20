package com.wurple.presentation.flow.previews.adapter

import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.wurple.R
import com.wurple.databinding.ItemPreviewBinding
import com.wurple.domain.model.preview.PreviewVisibilityValue
import com.wurple.presentation.extension.gone
import com.wurple.presentation.extension.visible
import com.wurple.presentation.flow.base.adapter.BaseViewHolder
import com.wurple.presentation.model.preview.PreviewListItem

class PreviewsViewHolder(
    private val binding: ItemPreviewBinding,
    private val onEditClickListener: (item: PreviewListItem) -> Unit,
    private val onShareClickListener: (item: PreviewListItem) -> Unit,
    private val onItemClickListener: (item: PreviewListItem) -> Unit
) : BaseViewHolder<PreviewListItem>(binding.root) {
    override fun onBind(item: PreviewListItem) {
        with(binding) {
            val context = root.context
            textView.text = item.title
            if (item.isExpired) {
                subtextView.text = context.getString(R.string.previews_add_expired)
                binding.visibilityValuesChipGroup.removeAllViews()
                binding.visibilityValuesChipGroup.gone()
            } else {
                subtextView.text = context.resources.getQuantityString(
                    R.plurals.previews_add_available_days,
                    item.differenceBetweenCurrentDateAndExpDate,
                    item.formattedExpDate,
                    item.differenceBetweenCurrentDateAndExpDate
                )
                binding.visibilityValuesChipGroup.removeAllViews()
                binding.visibilityValuesChipGroup.visible()
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_image),
                    item.userImageVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_name),
                    item.userNameVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_current_experience),
                    item.userCurrentPositionVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_location),
                    item.userLocationVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_email),
                    item.userEmailVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_username),
                    item.userUsernameVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_bio),
                    item.userBioVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_skills),
                    item.userSkillsVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_experience),
                    item.userExperienceVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_education),
                    item.userEducationVisibility
                )
                addVisibilityValuesChip(
                    context.getString(R.string.previews_card_languages),
                    item.userLanguagesVisibility
                )
            }
            shareButton.isVisible = item.isExpired.not()
            editButton.setOnClickListener { onEditClickListener(item) }
            shareButton.setOnClickListener { onShareClickListener(item) }
            container.setOnClickListener { onItemClickListener(item) }
        }
    }

    private fun addVisibilityValuesChip(
        text: String,
        previewVisibilityValue: PreviewVisibilityValue?
    ) {
        val context = binding.root.context
        previewVisibilityValue?.let {
            binding.visibilityValuesChipGroup.addView(
                Chip(context).apply {
                    this.text = text
                    isClickable = false
                    setChipBackgroundColorResource(
                        when (it) {
                            PreviewVisibilityValue.Visible -> R.color.colorGreen
                            PreviewVisibilityValue.PartiallyVisible -> R.color.colorYellow
                            PreviewVisibilityValue.Hidden -> R.color.colorRed
                        }
                    )
                    setEnsureMinTouchTargetSize(false)
                }
            )
        }
    }
}
