package com.wurple.data.model.user

import com.wurple.domain.extension.empty
import com.wurple.domain.model.common.StatusValue

data class UserRemoteData(
    val id: String = String.empty,
    val referrerId: String = String.empty,
    val firstName: String = String.empty,
    val lastName: String = String.empty,
    val email: String = String.empty,
    val imageUrl: String = String.empty,
    val dob: String = String.empty,
    val locationId: String = String.empty,
    val username: String = String.empty,
    val bio: String = String.empty,
    // json object
    val skills: String = String.empty,
    // json object
    val experience: String = String.empty,
    // json object
    val education: String = String.empty,
    // json object
    val languages: String = String.empty,
    // properties
    val statusValue: Int = StatusValue.Enabled.value,
    val statusDescription: String = String.empty,
    val createdAt: String = String.empty,
    val updatedAt: String = String.empty
) {
    companion object {
        const val COLLECTION = "user"
        const val FIELD_FIRST_NAME: String = "firstName"
        const val FIELD_LAST_NAME: String = "lastName"
        const val FIELD_EMAIL: String = "email"
        const val FIELD_IMAGE_URL: String = "imageUrl"
        const val FIELD_DOB: String = "dob"
        const val FIELD_LOCATION_ID: String = "locationId"
        const val FIELD_USERNAME: String = "username"
        const val FIELD_BIO: String = "bio"
        const val FIELD_SKILLS: String = "skills"
        const val FIELD_EXPERIENCE: String = "experience"
        const val FIELD_EDUCATION: String = "education"
        const val FIELD_LANGUAGES: String = "languages"
        const val FIELD_UPDATED_AT: String = "updatedAt"
        private const val PATH_IMAGE: String = "$COLLECTION/"

        fun generateImagePath(uid: String): String = "$PATH_IMAGE$uid"
    }
}