package com.wurple.data.model.preview

import com.wurple.domain.extension.empty
import com.wurple.domain.model.common.StatusValue
import com.wurple.domain.model.preview.PreviewLifetimeOption
import com.wurple.domain.model.preview.PreviewVisibilityValue
import java.util.*

data class PreviewRemoteData(
    val id: String = String.empty,
    val title: String = String.empty,
    val previewLifetimeOption: Int = PreviewLifetimeOption.Option1.value,
    val expDate: String = String.empty,
    val isExpired: Boolean = false,
    val userId: String = String.empty,

    // visibility values
    val userImageVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userNameVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userCurrentPositionVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userLocationVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userEmailVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userUsernameVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userBioVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userSkillsVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userExperienceVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userEducationVisibility: Int = PreviewVisibilityValue.Hidden.value,
    val userLanguagesVisibility: Int = PreviewVisibilityValue.Hidden.value,

    // properties
    val statusValue: Int = StatusValue.Enabled.value,
    val statusDescription: String = String.empty,
    val createdAt: String = String.empty,
    val updatedAt: String = String.empty
) {
    companion object {
        const val COLLECTION = "preview"
        const val FIELD_USER_ID = "userId"
        const val FIELD_TITLE = "title"
        const val FIELD_PREVIEW_LIFETIME_OPTION = "previewLifetimeOption"
        const val FIELD_EXP_DATE = "expDate"
        const val FIELD_USER_IMAGE_VISIBILITY = "userImageVisibility"
        const val FIELD_USER_NAME_VISIBILITY = "userNameVisibility"
        const val FIELD_USER_CURRENT_POSITION_VISIBILITY = "userCurrentPositionVisibility"
        const val FIELD_USER_LOCATION_VISIBILITY = "userLocationVisibility"
        const val FIELD_USER_EMAIL_VISIBILITY = "userEmailVisibility"
        const val FIELD_USER_USERNAME_VISIBILITY = "userUsernameVisibility"
        const val FIELD_USER_BIO_VISIBILITY = "userBioVisibility"
        const val FIELD_USER_SKILLS_VISIBILITY = "userSkillsVisibility"
        const val FIELD_USER_EXPERIENCE_VISIBILITY = "userExperienceVisibility"
        const val FIELD_USER_EDUCATION_VISIBILITY = "userEducationVisibility"
        const val FIELD_USER_LANGUAGES_VISIBILITY = "userLanguagesVisibility"
        const val FIELD_UPDATED_AT = "updatedAt"

        fun generatePreviewId(): String = "${COLLECTION}_${UUID.randomUUID()}"
    }
}