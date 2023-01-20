package com.wurple.presentation.model.preview

import androidx.annotation.ColorInt

data class PreviewSecondaryInfoUi(
    // UserLocation
    val userLocation: String,
    val isUserLocationVisible: Boolean,
    @ColorInt val userLocationButtonColor: Int? = null,
    val isUserLocationButtonVisible: Boolean = false,
    // UserEmail
    val userEmail: String,
    val isUserEmailVisible: Boolean = true,
    val isUserEmailCopyEnabled: Boolean = false,
    @ColorInt val userEmailButtonColor: Int? = null,
    val isUserEmailButtonVisible: Boolean = false,
    // UserUsername
    val userUsername: String,
    val isUserUsernameVisible: Boolean,
    @ColorInt val userUsernameButtonColor: Int? = null,
    val isUserUsernameButtonVisible: Boolean = false,
    // UserBio
    val userBio: String,
    val isUserBioVisible: Boolean,
    @ColorInt val userBioButtonColor: Int? = null,
    val isUserBioButtonVisible: Boolean = false
)