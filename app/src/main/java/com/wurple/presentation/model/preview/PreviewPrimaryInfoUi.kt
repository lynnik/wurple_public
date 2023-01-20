package com.wurple.presentation.model.preview

import androidx.annotation.ColorInt

data class PreviewPrimaryInfoUi(
    // UserImage
    val userImageUrl: String,
    @ColorInt val userImageButtonColor: Int? = null,
    val isUserImageButtonVisible: Boolean = false,
    // UserName
    val userName: String,
    @ColorInt val userNameButtonColor: Int? = null,
    val isUserNameButtonVisible: Boolean = false,
    // UserExperience
    val userExperiencePosition: String,
    val userExperienceCompanyName: String,
    val isUserExperienceVisible: Boolean,
    @ColorInt val userExperienceButtonColor: Int? = null,
    val isUserExperienceButtonVisible: Boolean = false
)