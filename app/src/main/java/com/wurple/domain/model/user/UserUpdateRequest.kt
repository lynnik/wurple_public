package com.wurple.domain.model.user

data class UserUpdateRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val imageUrl: String? = null,
    val dob: String? = null,
    val locationId: String? = null,
    val username: String? = null,
    val bio: String? = null,
    val languages: List<UserLanguage>? = null
) {
    companion object {
        const val REMOVE_IMAGE_VALUE = "removeImage"
    }
}