package com.wurple.presentation.model.preview

import androidx.annotation.ColorInt

data class PreviewExperienceUi(
    val items: List<ListItem>,
    val isUserExperienceVisible: Boolean,
    @ColorInt val userExperienceButtonColor: Int? = null,
    val isUserExperienceButtonVisible: Boolean = false
) {
    data class ListItem(
        val position: String,
        val companyName: String,
        val dateRange: String
    )
}