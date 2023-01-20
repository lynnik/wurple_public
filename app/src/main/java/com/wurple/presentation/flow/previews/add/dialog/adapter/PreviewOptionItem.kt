package com.wurple.presentation.flow.previews.add.dialog.adapter

import com.wurple.domain.extension.empty
import com.wurple.domain.model.preview.PreviewVisibilityValue

sealed class PreviewOptionItem(
    val previewVisibilityValue: PreviewVisibilityValue,
    val subtext: String,
    val isChecked: Boolean
) {
    data class Visible(
        val visibleSubtext: String,
        val visibleIsChecked: Boolean
    ) : PreviewOptionItem(
        previewVisibilityValue = PreviewVisibilityValue.Visible,
        subtext = visibleSubtext,
        isChecked = visibleIsChecked
    )

    data class PartiallyVisible(
        val partiallyVisibleSubtext: String,
        val partiallyVisibleIsChecked: Boolean
    ) : PreviewOptionItem(
        previewVisibilityValue = PreviewVisibilityValue.PartiallyVisible,
        subtext = partiallyVisibleSubtext,
        isChecked = partiallyVisibleIsChecked
    )

    data class Hidden(
        val hiddenIsChecked: Boolean
    ) : PreviewOptionItem(
        previewVisibilityValue = PreviewVisibilityValue.Hidden,
        subtext = String.empty,
        isChecked = hiddenIsChecked
    )
}