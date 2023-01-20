package com.wurple.domain.model.user

import com.wurple.domain.model.common.Properties
import com.wurple.domain.model.location.Location

data class User(
    val id: String,
    val referrerId: String,
    val userName: UserName?,
    val userEmail: String,
    val userImage: UserImage?,
    val userDob: UserDob?,
    val userLocation: Location?,
    val userUsername: String,
    val userBio: String,
    val userSkills: List<UserSkill>,
    val userExperience: List<UserExperience>,
    val userEducation: List<UserEducation>,
    val userLanguages: List<UserLanguage>,
    val properties: Properties
) {
    fun getSortedUserSkills(): List<UserSkill> {
        return userSkills.sortedWith(compareBy { it.title })
    }

    fun getSortedUserExperience(): List<UserExperience> {
        return userExperience.sortedWith(
            compareByDescending<UserExperience> { it.isCurrent }
                .thenByDescending { it.fromDate.baseDate.toString() }
                .thenBy { it.position }
                .thenBy { it.companyName }
        )
    }

    fun getSortedUserEducation(): List<UserEducation> {
        return userEducation.sortedWith(
            compareByDescending<UserEducation> { it.graduationDate.baseDate.toString() }
                .thenBy { it.degree }
                .thenBy { it.institution }
        )
    }

    fun getSortedUserLanguages(): List<UserLanguage> {
        return userLanguages.sortedWith(
            compareBy<UserLanguage> { it.language.name }
                .thenByDescending { it.languageLevel.id }
        )
    }

    fun getCurrentUserExperience(): UserExperience? {
        return userExperience
            .filter { it.isCurrent }
            .maxByOrNull { it.fromDate.baseDate.toString() }
    }

    companion object {
        const val MIN_AGE = 16
        const val MAX_AGE = 100
    }
}