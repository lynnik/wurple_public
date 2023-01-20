package com.wurple.domain.model.preview

import com.wurple.domain.model.common.Properties
import com.wurple.domain.model.date.Date

data class Preview(
    val id: String,
    val title: String,
    val previewLifetimeOption: PreviewLifetimeOption,
    val expDate: Date,
    val isExpired: Boolean,

    val userId: String,
    // visibility values
    val userImageVisibility: PreviewVisibilityValue,
    val userNameVisibility: PreviewVisibilityValue,
    val userCurrentPositionVisibility: PreviewVisibilityValue,
    val userLocationVisibility: PreviewVisibilityValue,
    val userEmailVisibility: PreviewVisibilityValue,
    val userUsernameVisibility: PreviewVisibilityValue,
    val userBioVisibility: PreviewVisibilityValue,
    val userSkillsVisibility: PreviewVisibilityValue,
    val userExperienceVisibility: PreviewVisibilityValue,
    val userEducationVisibility: PreviewVisibilityValue,
    val userLanguagesVisibility: PreviewVisibilityValue,

    val properties: Properties
)