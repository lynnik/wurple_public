package com.wurple.presentation.model.preview

import com.wurple.domain.model.preview.PreviewVisibilityValue

data class PreviewListItem(
    val previewId: String,
    val title: String,
    val formattedExpDate: String,
    val differenceBetweenCurrentDateAndExpDate: Int,
    val isExpired: Boolean,
    // visibility values
    val userImageVisibility: PreviewVisibilityValue,
    val userNameVisibility: PreviewVisibilityValue,
    val userCurrentPositionVisibility: PreviewVisibilityValue? = null,
    val userLocationVisibility: PreviewVisibilityValue? = null,
    val userEmailVisibility: PreviewVisibilityValue,
    val userUsernameVisibility: PreviewVisibilityValue? = null,
    val userBioVisibility: PreviewVisibilityValue? = null,
    val userSkillsVisibility: PreviewVisibilityValue? = null,
    val userExperienceVisibility: PreviewVisibilityValue? = null,
    val userEducationVisibility: PreviewVisibilityValue? = null,
    val userLanguagesVisibility: PreviewVisibilityValue? = null
)