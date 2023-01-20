package com.wurple.presentation.model.mapper

import android.content.Context
import com.wurple.R
import com.wurple.domain.extension.empty
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.preview.PreviewVisibilityValue
import com.wurple.domain.model.preview.UserTempPreview
import com.wurple.domain.model.user.UserSkill
import com.wurple.presentation.extension.getColorCompat
import com.wurple.presentation.model.preview.*

class FromUserTempPreviewToPreviewUiOnAddMapper(
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
                userImageButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userImageVisibility),
                isUserImageButtonVisible = true,
                userName = when (from.createPreviewRequest?.userNameVisibility) {
                    PreviewVisibilityValue.Visible -> from.user.userName?.fullName ?: String.empty
                    PreviewVisibilityValue.PartiallyVisible ->
                        from.user.userName?.shortFullName ?: String.empty
                    PreviewVisibilityValue.Hidden ->
                        context.getString(R.string.previews_add_user_name_hidden)
                    else -> context.getString(R.string.previews_add_user_name_hidden)
                },
                userNameButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userNameVisibility),
                isUserNameButtonVisible = true,
                userExperiencePosition = when (from.createPreviewRequest?.userCurrentPositionVisibility) {
                    PreviewVisibilityValue.Visible,
                    PreviewVisibilityValue.PartiallyVisible ->
                        currentUserExperience?.position ?: String.empty
                    PreviewVisibilityValue.Hidden ->
                        context.getString(R.string.previews_add_current_position_hidden)
                    else -> context.getString(R.string.previews_add_current_position_hidden)
                },
                userExperienceCompanyName = when (from.createPreviewRequest?.userCurrentPositionVisibility) {
                    PreviewVisibilityValue.Visible ->
                        currentUserExperience?.companyName ?: String.empty
                    else -> String.empty
                },
                isUserExperienceVisible = from.createPreviewRequest?.userCurrentPositionVisibility == PreviewVisibilityValue.Visible,
                userExperienceButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userCurrentPositionVisibility),
                isUserExperienceButtonVisible = true
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
                isUserLocationVisible = from.user.userLocation != null,
                userLocationButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userLocationVisibility),
                isUserLocationButtonVisible = true,

                userEmail = when (from.createPreviewRequest?.userEmailVisibility) {
                    PreviewVisibilityValue.Visible -> from.user.userEmail
                    else -> context.getString(R.string.common_hidden)
                },
                userEmailButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userEmailVisibility),
                isUserEmailButtonVisible = true,

                userUsername = when (from.createPreviewRequest?.userUsernameVisibility) {
                    PreviewVisibilityValue.Visible -> context.getString(
                        R.string.common_username_with_prefix,
                        from.user.userUsername
                    )
                    else -> context.getString(R.string.common_hidden)
                },
                isUserUsernameVisible = from.user.userUsername.isNotEmpty(),
                userUsernameButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userUsernameVisibility),
                isUserUsernameButtonVisible = true,

                userBio = when (from.createPreviewRequest?.userBioVisibility) {
                    PreviewVisibilityValue.Visible -> from.user.userBio
                    else -> context.getString(R.string.common_hidden)
                },
                isUserBioVisible = from.user.userBio.isNotEmpty(),
                userBioButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userBioVisibility),
                isUserBioButtonVisible = true
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
                isUserSkillsVisible = from.user.userSkills.isNotEmpty(),
                userSkillsButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userSkillsVisibility),
                isUserSkillsButtonVisible = true
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
                isUserExperienceVisible = from.user.userExperience.isNotEmpty(),
                userExperienceButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userExperienceVisibility),
                isUserExperienceButtonVisible = true
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
                isUserEducationVisible = from.user.userEducation.isNotEmpty(),
                userEducationButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userEducationVisibility),
                isUserEducationButtonVisible = true
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
                isUserLanguagesVisible = from.user.userLanguages.isNotEmpty(),
                userLanguagesButtonColor = getColorByPreviewVisibilityValue(from.createPreviewRequest?.userLanguagesVisibility),
                isUserLanguagesButtonVisible = true
            )
        )
    }

    private fun getColorByPreviewVisibilityValue(value: PreviewVisibilityValue?): Int {
        return context.getColorCompat(
            when (value) {
                PreviewVisibilityValue.Visible -> R.color.colorGreen
                PreviewVisibilityValue.PartiallyVisible -> R.color.colorYellow
                PreviewVisibilityValue.Hidden -> R.color.colorRed
                else -> R.color.colorRed
            }
        )
    }
}