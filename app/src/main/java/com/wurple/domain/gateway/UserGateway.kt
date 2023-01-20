package com.wurple.domain.gateway

import com.wurple.domain.model.user.*
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserGateway {
    suspend fun getUserById(id: String): User
    suspend fun isCurrentUserExist(): Boolean
    suspend fun createUser(referrerId: String?)
    suspend fun getCurrentUserId(): String
    suspend fun getCurrentUser(): User
    suspend fun getCurrentUserFromStorage(): User
    suspend fun fetchCurrentUser()
    fun observeCurrentUser(): Flow<User>
    suspend fun getUserImageFile(): File
    suspend fun updateCurrentUser(updateRequest: UserUpdateRequest)
    suspend fun updateCurrentUserEmail(email: String)
    suspend fun updateCurrentUserSkills(updateRequest: UserSkillsUpdateRequest)
    suspend fun updateCurrentUserExperience(updateRequest: UserExperienceUpdateRequest)
    suspend fun updateCurrentUserEducation(updateRequest: UserEducationUpdateRequest)
    suspend fun clearCache()
}