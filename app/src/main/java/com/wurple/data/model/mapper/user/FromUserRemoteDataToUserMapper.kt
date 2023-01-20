package com.wurple.data.model.mapper.user

import android.content.Context
import com.wurple.R
import com.wurple.data.model.user.UserRemoteData
import com.wurple.domain.date.DateManager
import com.wurple.domain.education.UserEducationManager
import com.wurple.domain.experience.UserExperienceManager
import com.wurple.domain.extension.empty
import com.wurple.domain.language.UserLanguageManager
import com.wurple.domain.location.LocationManager
import com.wurple.domain.model.common.Properties
import com.wurple.domain.model.common.Status
import com.wurple.domain.model.common.StatusValue
import com.wurple.domain.model.date.Date
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserDob
import com.wurple.domain.model.user.UserImage
import com.wurple.domain.model.user.UserName
import com.wurple.domain.skills.UserSkillsManager

class FromUserRemoteDataToUserMapper(
    private val context: Context,
    private val locationManager: LocationManager,
    private val dateManager: DateManager,
    private val userSkillsManager: UserSkillsManager,
    private val userExperienceManager: UserExperienceManager,
    private val userEducationManager: UserEducationManager,
    private val userLanguageManager: UserLanguageManager
) : Mapper<UserRemoteData, User> {
    override suspend fun map(from: UserRemoteData): User {
        val userName = if (from.firstName.isEmpty() || from.lastName.isEmpty()) {
            null
        } else {
            UserName(
                firstName = from.firstName,
                lastName = from.lastName,
                fullName = context.getString(
                    R.string.common_user_full_name,
                    from.firstName,
                    from.lastName
                ),
                shortFullName = context.getString(
                    R.string.common_user_full_name,
                    from.firstName,
                    from.lastName.firstOrNull() ?: String.empty
                )
            )
        }
        val userImage = if (from.imageUrl.isEmpty()) {
            null
        } else {
            UserImage(url = from.imageUrl)
        }
        val userDob = if (from.dob.isEmpty()) {
            null
        } else {
            val dobBaseDate = dateManager.convertStringDateToBaseDate(from.dob)
            UserDob(
                date = Date(
                    baseDate = dobBaseDate,
                    formattedDate = dateManager.getDefaultFormattedDate(dobBaseDate)
                ),
                age = dateManager.getAgeFromBaseDate(dobBaseDate)
            )
        }
        val userLocation = if (from.locationId.isEmpty()) {
            null
        } else {
            locationManager.getLocationById(from.locationId)
        }
        val userSkills = userSkillsManager.getSkills(from.skills)
        val userExperience = userExperienceManager.getExperience(from.experience)
        val userEducation = userEducationManager.getEducation(from.education)
        val userLanguages = userLanguageManager.getLanguages(from.languages)
        val createdAtBaseDate = dateManager.convertStringDateToBaseDate(from.createdAt)
        val updatedAtBaseDate = dateManager.convertStringDateToBaseDate(from.updatedAt)
        return User(
            id = from.id,
            referrerId = from.referrerId,
            userName = userName,
            userEmail = from.email,
            userImage = userImage,
            userDob = userDob,
            userLocation = userLocation,
            userUsername = from.username,
            userBio = from.bio,
            userSkills = userSkills,
            userExperience = userExperience,
            userEducation = userEducation,
            userLanguages = userLanguages,
            properties = Properties(
                status = Status(
                    value = StatusValue.getByValue(from.statusValue),
                    description = from.statusDescription
                ),
                createdAt = Date(
                    baseDate = createdAtBaseDate,
                    formattedDate = dateManager.getDefaultFormattedDate(createdAtBaseDate)
                ),
                updatedAt = Date(
                    baseDate = updatedAtBaseDate,
                    formattedDate = dateManager.getDefaultFormattedDate(updatedAtBaseDate)
                )
            )
        )
    }
}