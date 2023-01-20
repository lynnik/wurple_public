package com.wurple.data.model.mapper.user

import com.wurple.data.model.user.UserStorageData
import com.wurple.domain.date.DateManager
import com.wurple.domain.education.UserEducationManager
import com.wurple.domain.experience.UserExperienceManager
import com.wurple.domain.extension.empty
import com.wurple.domain.language.UserLanguageManager
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.User
import com.wurple.domain.skills.UserSkillsManager

class FromUserToUserStorageDataMapper(
    private val dateManager: DateManager,
    private val userSkillsManager: UserSkillsManager,
    private val userExperienceManager: UserExperienceManager,
    private val userEducationManager: UserEducationManager,
    private val userLanguageManager: UserLanguageManager
) : Mapper<User, UserStorageData> {
    override suspend fun map(from: User): UserStorageData {
        val firstName = from.userName?.firstName ?: String.empty
        val lastName = from.userName?.lastName ?: String.empty
        val imageUrl = from.userImage?.url ?: String.empty
        val dob = from.userDob
            ?.let { dateManager.convertBaseDateToStringDate(it.date.baseDate) }
            ?: String.empty
        val locationId = from.userLocation?.id ?: String.empty
        val userSkills = userSkillsManager.getJsonOfSkillsList(from.userSkills)
        val experience = userExperienceManager.getJsonOfExperienceList(from.userExperience)
        val education = userEducationManager.getJsonOfEducationList(from.userEducation)
        val languages = userLanguageManager.getJsonOfLanguagesList(from.userLanguages)
        return UserStorageData(
            id = from.id,
            referrerId = from.referrerId,
            firstName = firstName,
            lastName = lastName,
            email = from.userEmail,
            imageUrl = imageUrl,
            dob = dob,
            locationId = locationId,
            city = from.userLocation?.city ?: String.empty,
            country = from.userLocation?.country ?: String.empty,
            username = from.userUsername,
            bio = from.userBio,
            skills = userSkills,
            experience = experience,
            education = education,
            languages = languages,
            // properties
            statusValue = from.properties.status.value.value,
            statusDescription = from.properties.status.description,
            createdAt = dateManager.convertBaseDateToStringDate(from.properties.createdAt.baseDate),
            updatedAt = dateManager.convertBaseDateToStringDate(from.properties.updatedAt.baseDate)
        )
    }
}