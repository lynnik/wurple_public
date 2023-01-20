package com.wurple.presentation.model.preview

import androidx.annotation.ColorInt

data class PreviewSkillsUi(
    val personalSkills: List<String>,
    val professionalSkills: List<String>,
    val isUserSkillsVisible: Boolean,
    @ColorInt val userSkillsButtonColor: Int? = null,
    val isUserSkillsButtonVisible: Boolean = false
)