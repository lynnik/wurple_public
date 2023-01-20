package com.wurple.data.model.language

import androidx.annotation.StringRes

data class LanguageLevelData(
    val id: Int,
    @StringRes val nameStringResId: Int
)