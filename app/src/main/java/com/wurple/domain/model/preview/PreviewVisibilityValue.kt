package com.wurple.domain.model.preview

sealed class PreviewVisibilityValue(val value: Int) {
    object Visible : PreviewVisibilityValue(0)
    object PartiallyVisible : PreviewVisibilityValue(1)
    object Hidden : PreviewVisibilityValue(2)

    companion object {
        fun getByValue(value: Int): PreviewVisibilityValue {
            return when (value) {
                Visible.value -> Visible
                PartiallyVisible.value -> PartiallyVisible
                Hidden.value -> Hidden
                else -> throw IllegalArgumentException(
                    "There is no PreviewVisibilityValue with value: $value"
                )
            }
        }
    }
}