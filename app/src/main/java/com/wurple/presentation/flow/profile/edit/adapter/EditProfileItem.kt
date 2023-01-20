package com.wurple.presentation.flow.profile.edit.adapter

sealed class EditProfileItem {
    data class Image(val imageUrl: String?) : EditProfileItem()
    data class UserFullName(val text: String) : EditProfileItem()
    data class UserDob(val text: String) : EditProfileItem()
    data class UserLocation(val text: String) : EditProfileItem()
    data class UserEmail(val text: String) : EditProfileItem()
    data class UserUsername(val text: String) : EditProfileItem()
    data class UserBio(val text: String) : EditProfileItem()
    object UserSkills : EditProfileItem()
    object UserExperience : EditProfileItem()
    object UserEducation : EditProfileItem()
    object UserLanguages : EditProfileItem()
}