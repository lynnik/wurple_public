package com.wurple.data.remote.user

import com.wurple.domain.model.user.*

interface UserRemoteDataSource {
    suspend fun getUserById(id: String): User
    suspend fun isCurrentUserExist(currentUserId: String): Boolean
    suspend fun createUser(currentUserId: String, referrerId: String?)
    suspend fun getCurrentUser(currentUserId: String): User
    suspend fun updateCurrentUser(currentUserId: String, updateRequest: UserUpdateRequest): User
    suspend fun updateCurrentUserEmail(currentUserId: String, email: String): User
    suspend fun updateCurrentUserSkills(
        currentUserId: String,
        updateRequest: UserSkillsUpdateRequest,
        oldUserSkills: List<UserSkill>
    ): User

    suspend fun updateCurrentUserExperience(
        currentUserId: String,
        updateRequest: UserExperienceUpdateRequest,
        oldUserExperience: List<UserExperience>
    ): User

    suspend fun updateCurrentUserEducation(
        currentUserId: String,
        updateRequest: UserEducationUpdateRequest,
        oldUserEducation: List<UserEducation>
    ): User
}