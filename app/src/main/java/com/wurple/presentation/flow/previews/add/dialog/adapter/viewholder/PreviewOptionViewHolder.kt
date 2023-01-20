package com.wurple.presentation.flow.previews.add.dialog.adapter.viewholder

import android.graphics.PorterDuff
import androidx.core.view.isVisible
import com.wurple.R
import com.wurple.databinding.ItemPreviewOptionBinding
import com.wurple.domain.model.preview.PreviewVisibilityValue
import com.wurple.presentation.extension.getColorCompat
import com.wurple.presentation.flow.base.adapter.BaseViewHolder
import com.wurple.presentation.flow.previews.add.dialog.adapter.PreviewOptionItem

class PreviewOptionViewHolder(
    private val binding: ItemPreviewOptionBinding,
    private val onItemClickListener: (previewVisibilityValue: PreviewVisibilityValue) -> Unit
) : BaseViewHolder<PreviewOptionItem>(binding.root) {
    override fun onBind(item: PreviewOptionItem) {
        val context = binding.root.context
        binding.legendImageView.setColorFilter(
            when (item) {
                is PreviewOptionItem.Visible -> context.getColorCompat(R.color.colorGreen)
                is PreviewOptionItem.PartiallyVisible -> context.getColorCompat(R.color.colorYellow)
                is PreviewOptionItem.Hidden -> context.getColorCompat(R.color.colorRed)
            },
            PorterDuff.Mode.SRC_IN
        )
        binding.textView.text = context.getString(
            when (item) {
                is PreviewOptionItem.Visible -> R.string.previews_add_option_value_visible
                is PreviewOptionItem.PartiallyVisible -> R.string.previews_add_option_value_partially_visible
                is PreviewOptionItem.Hidden -> R.string.previews_add_option_value_hidden
            }
        )
        binding.subtextView.text = item.subtext
        binding.subtextView.isVisible = item.subtext.isNotEmpty()
        binding.imageView.isVisible = item.isChecked
        binding.container.setOnClickListener { onItemClickListener(item.previewVisibilityValue) }
    }
}