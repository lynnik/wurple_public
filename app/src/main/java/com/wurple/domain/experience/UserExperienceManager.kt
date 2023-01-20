package com.wurple.domain.experience

import com.wurple.domain.model.user.UserExperience

interface UserExperienceManager {
    suspend fun getExperience(json: String): List<UserExperience>
    suspend fun getJsonOfExperienceList(userExperience: List<UserExperience>): String
}