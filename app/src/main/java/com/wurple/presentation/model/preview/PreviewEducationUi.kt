package com.wurple.presentation.model.preview

import androidx.annotation.ColorInt

data class PreviewEducationUi(
    val items: List<ListItem>,
    val isUserEducationVisible: Boolean,
    @ColorInt val userEducationButtonColor: Int? = null,
    val isUserEducationButtonVisible: Boolean = false
) {
    data class ListItem(
        val degree: String,
        val institution: String,
        val graduationDate: String
    )
}