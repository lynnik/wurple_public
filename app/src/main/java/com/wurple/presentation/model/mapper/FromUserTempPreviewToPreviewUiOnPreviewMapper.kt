package com.wurple.presentation.model.mapper

import android.content.Context
import com.wurple.R
import com.wurple.domain.extension.empty
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.preview.PreviewVisibilityValue
import com.wurple.domain.model.preview.UserTempPreview
import com.wurple.domain.model.user.UserSkill
import com.wurple.presentation.model.preview.*

class FromUserTempPreviewToPreviewUiOnPreviewMapper(
    private val context: Context
) : Mapper<UserTempPreview, PreviewUi> {
    override suspend fun map(from: UserTempPreview): PreviewUi {
        val currentUserExperience = from.user.getCurrentUserExperience()
        return PreviewUi(
            primaryInfo = PreviewPrimaryInfoUi(
                userImageUrl = when (from.createPreviewRequest?.userImageVisibility) {
                    PreviewVisibilityValue.Visible -> from.user.userImage?.url ?: String.empty
                    else -> String.empty
                },
                userName = when (from.createPreviewRequest?.userNameVisibility) {
                    PreviewVisibilityValue.Visible -> from.user.userName?.fullName ?: String.empty
                    PreviewVisibilityValue.PartiallyVisible ->
                        from.user.userName?.shortFullName ?: String.empty
                    PreviewVisibilityValue.Hidden -> context.getString(
                        R.string.common_user_full_name,
                        context.getString(R.string.edit_profile_name_first_name_stub),
                        context.getString(R.string.edit_profile_name_last_name_stub)
                    )
                    else -> context.getString(
                        R.string.common_user_full_name,
                        context.getString(R.string.edit_profile_name_first_name_stub),
                        context.getString(R.string.edit_profile_name_last_name_stub)
                    )
                },
                userExperiencePosition = when (from.createPreviewRequest?.userCurrentPositionVisibility) {
                    PreviewVisibilityValue.Visible,
                    PreviewVisibilityValue.PartiallyVisible ->
                        currentUserExperience?.position ?: String.empty
                    PreviewVisibilityValue.Hidden -> String.empty
                    else -> String.empty
                },
                userExperienceCompanyName = when (from.createPreviewRequest?.userCurrentPositionVisibility) {
                    PreviewVisibilityValue.Visible ->
                        currentUserExperience?.companyName ?: String.empty
                    else -> String.empty
                },
                isUserExperienceVisible = from.createPreviewRequest?.userCurrentPositionVisibility != PreviewVisibilityValue.Hidden
            ),
            secondaryInfo = PreviewSecondaryInfoUi(
                userLocation = when (from.createPreviewRequest?.userLocationVisibility) {
                    PreviewVisibilityValue.Visible ->
                        from.user.userLocation?.formattedLocation ?: String.empty
                    PreviewVisibilityValue.PartiallyVisible ->
                        from.user.userLocation?.country ?: String.empty
                    PreviewVisibilityValue.Hidden -> context.getString(R.string.common_hidden)
                    else -> context.getString(R.string.common_hidden)
                },
                isUserLocationVisible = from.user.userLocation != null &&
                        from.createPreviewRequest?.userLocationVisibility != PreviewVisibilityValue.Hidden,

                userEmail = when (from.createPreviewRequest?.userEmailVisibility) {
                    PreviewVisibilityValue.Visible -> from.user.userEmail
                    else -> context.getString(R.string.common_hidden)
                },
                isUserEmailVisible = from.createPreviewRequest?.userEmailVisibility != PreviewVisibilityValue.Hidden,

                userUsername = when (from.createPreviewRequest?.userUsernameVisibility) {
                    PreviewVisibilityValue.Visible -> context.getString(
                        R.string.common_username_with_prefix,
                        from.user.userUsername
                    )
                    else -> context.getString(R.string.common_hidden)
                },
                isUserUsernameVisible = from.user.userUsername.isNotEmpty() &&
                        from.createPreviewRequest?.userUsernameVisibility != PreviewVisibilityValue.Hidden,

                userBio = when (from.createPreviewRequest?.userBioVisibility) {
                    PreviewVisibilityValue.Visible -> from.user.userBio
                    else -> context.getString(R.string.common_hidden)
                },
                isUserBioVisible = from.user.userBio.isNotEmpty() &&
                        from.createPreviewRequest?.userBioVisibility != PreviewVisibilityValue.Hidden
            ),
            skills = PreviewSkillsUi(
                personalSkills = if (from.createPreviewRequest?.userSkillsVisibility == PreviewVisibilityValue.Hidden) {
                    listOf()
                } else {
                    from.user.getSortedUserSkills()
                        .filter { it.type == UserSkill.Type.Personal }
                        .map { it.title }
                },
                professionalSkills = if (from.createPreviewRequest?.userSkillsVisibility == PreviewVisibilityValue.Hidden) {
                    listOf()
                } else {
                    from.user.getSortedUserSkills()
                        .filter { it.type == UserSkill.Type.Professional }
                        .map { it.title }
                },
                isUserSkillsVisible = from.user.userSkills.isNotEmpty() &&
                        from.createPreviewRequest?.userSkillsVisibility != PreviewVisibilityValue.Hidden
            ),
            experience = PreviewExperienceUi(
                items = if (from.createPreviewRequest?.userExperienceVisibility == PreviewVisibilityValue.Hidden) {
                    listOf()
                } else {
                    from.user.getSortedUserExperience().map {
                        PreviewExperienceUi.ListItem(
                            position = it.position,
                            companyName = when (from.createPreviewRequest?.userExperienceVisibility) {
                                PreviewVisibilityValue.Visible -> it.companyName
                                else -> String.empty
                            },
                            dateRange = it.fromToFormattedDateRange
                                ?.let { range ->
                                    context.getString(
                                        R.string.experience_date_range,
                                        it.fromDate.formattedDate,
                                        if (it.isCurrent) {
                                            context.getString(R.string.common_date_present)
                                        } else {
                                            it.toDate.formattedDate
                                        },
                                        range
                                    )
                                }
                                ?: run {
                                    context.getString(
                                        R.string.experience_date,
                                        it.fromDate.formattedDate,
                                        if (it.isCurrent) {
                                            context.getString(R.string.common_date_present)
                                        } else {
                                            it.toDate.formattedDate
                                        }
                                    )
                                }
                        )
                    }
                },
                isUserExperienceVisible = from.user.userExperience.isNotEmpty() &&
                        from.createPreviewRequest?.userExperienceVisibility != PreviewVisibilityValue.Hidden
            ),
            education = PreviewEducationUi(
                items = if (from.createPreviewRequest?.userEducationVisibility == PreviewVisibilityValue.Hidden) {
                    listOf()
                } else {
                    from.user.getSortedUserEducation().map {
                        PreviewEducationUi.ListItem(
                            degree = it.degree,
                            institution = when (from.createPreviewRequest?.userEducationVisibility) {
                                PreviewVisibilityValue.Visible -> it.institution
                                else -> String.empty
                            },
                            graduationDate = it.graduationDate.formattedDate
                        )
                    }
                },
                isUserEducationVisible = from.user.userEducation.isNotEmpty() &&
                        from.createPreviewRequest?.userEducationVisibility != PreviewVisibilityValue.Hidden
            ),
            languages = PreviewLanguagesUi(
                items = if (from.createPreviewRequest?.userLanguagesVisibility == PreviewVisibilityValue.Hidden) {
                    listOf()
                } else {
                    from.user.getSortedUserLanguages().map {
                        PreviewLanguagesUi.ListItem(
                            language = it.language.name,
                            languageLevel = it.languageLevel.name
                        )
                    }
                },
                isUserLanguagesVisible = from.user.userLanguages.isNotEmpty() &&
                        from.createPreviewRequest?.userLanguagesVisibility != PreviewVisibilityValue.Hidden
            )
        )
    }
}