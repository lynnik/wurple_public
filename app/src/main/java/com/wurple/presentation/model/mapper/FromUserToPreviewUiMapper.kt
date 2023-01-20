package com.wurple.presentation.model.mapper

import android.content.Context
import com.wurple.R
import com.wurple.domain.extension.empty
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserSkill
import com.wurple.presentation.model.preview.*

class FromUserToPreviewUiMapper(
    private val context: Context
) : Mapper<User, PreviewUi> {
    override suspend fun map(from: User): PreviewUi {
        val currentUserExperience = from.getCurrentUserExperience()
        return PreviewUi(
            primaryInfo = PreviewPrimaryInfoUi(
                userImageUrl = from.userImage?.url ?: String.empty,
                userName = from.userName?.fullName ?: String.empty,
                userExperiencePosition = currentUserExperience?.position ?: String.empty,
                userExperienceCompanyName = currentUserExperience?.companyName ?: String.empty,
                isUserExperienceVisible = currentUserExperience != null
            ),
            secondaryInfo = PreviewSecondaryInfoUi(
                userLocation = from.userLocation?.formattedLocation ?: String.empty,
                isUserLocationVisible = from.userLocation != null,
                userEmail = from.userEmail,
                userUsername = context.getString(
                    R.string.common_username_with_prefix,
                    from.userUsername
                ),
                isUserUsernameVisible = from.userUsername.isNotEmpty(),
                userBio = from.userBio,
                isUserBioVisible = from.userBio.isNotEmpty()
            ),
            skills = PreviewSkillsUi(
                personalSkills = from.getSortedUserSkills()
                    .filter { it.type == UserSkill.Type.Personal }
                    .map { it.title },
                professionalSkills = from.getSortedUserSkills()
                    .filter { it.type == UserSkill.Type.Professional }
                    .map { it.title },
                isUserSkillsVisible = from.userSkills.isNotEmpty()
            ),
            experience = PreviewExperienceUi(
                items = from.getSortedUserExperience().map {
                    PreviewExperienceUi.ListItem(
                        position = it.position,
                        companyName = it.companyName,
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
                },
                isUserExperienceVisible = from.userExperience.isNotEmpty()
            ),
            education = PreviewEducationUi(
                items = from.getSortedUserEducation().map {
                    PreviewEducationUi.ListItem(
                        degree = it.degree,
                        institution = it.institution,
                        graduationDate = it.graduationDate.formattedDate
                    )
                },
                isUserEducationVisible = from.userEducation.isNotEmpty()
            ),
            languages = PreviewLanguagesUi(
                items = from.getSortedUserLanguages().map {
                    PreviewLanguagesUi.ListItem(
                        language = it.language.name,
                        languageLevel = it.languageLevel.name
                    )
                },
                isUserLanguagesVisible = from.userLanguages.isNotEmpty()
            )
        )
    }
}