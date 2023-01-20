package com.wurple.presentation.model.preview

import androidx.annotation.ColorInt

data class PreviewLanguagesUi(
    val items: List<ListItem>,
    val isUserLanguagesVisible: Boolean,
    @ColorInt val userLanguagesButtonColor: Int? = null,
    val isUserLanguagesButtonVisible: Boolean = false
) {
    data class ListItem(
        val language: String,
        val languageLevel: String
    )
}