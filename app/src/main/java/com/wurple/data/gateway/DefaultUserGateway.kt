package com.wurple.data.gateway

import com.wurple.data.remote.user.UserRemoteDataSource
import com.wurple.data.storage.user.UserStorageDataSource
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.model.user.*
import kotlinx.coroutines.flow.Flow
import java.io.File

class DefaultUserGateway(
    private val userStorageDataSource: UserStorageDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserGateway {
    override suspend fun getUserById(id: String): User = userRemoteDataSource.getUserById(id)

    override suspend fun isCurrentUserExist(): Boolean {
        val currentUserId = userStorageDataSource.getCurrentUserId()
        return userRemoteDataSource.isCurrentUserExist(currentUserId)
    }

    override suspend fun createUser(referrerId: String?) {
        val currentUserId = userStorageDataSource.getCurrentUserId()
        userRemoteDataSource.createUser(currentUserId, referrerId)
    }

    override suspend fun getCurrentUserId(): String = userStorageDataSource.getCurrentUserId()

    override suspend fun getCurrentUser(): User {
        val currentUserId = userStorageDataSource.getCurrentUserId()
        return userRemoteDataSource.getCurrentUser(currentUserId)
    }

    override suspend fun getCurrentUserFromStorage(): User = userStorageDataSource.getCurrentUser()

    override suspend fun fetchCurrentUser() {
        val remoteCurrentUser = getCurrentUser()
        userStorageDataSource.saveCurrentUser(remoteCurrentUser)
    }

    override fun observeCurrentUser(): Flow<User> = userStorageDataSource.observeCurrentUser()

    override suspend fun getUserImageFile(): File = userStorageDataSource.getUserImageFile()

    override suspend fun updateCurrentUser(updateRequest: UserUpdateRequest) {
        val currentUserId = userStorageDataSource.getCurrentUserId()
        val remoteCurrentUser = userRemoteDataSource.updateCurrentUser(currentUserId, updateRequest)
        userStorageDataSource.saveCurrentUser(remoteCurrentUser)
    }

    override suspend fun updateCurrentUserEmail(email: String) {
        val currentUserId = userStorageDataSource.getCurrentUserId()
        val remoteCurrentUser = userRemoteDataSource.updateCurrentUserEmail(currentUserId, email)
        userStorageDataSource.saveCurrentUser(remoteCurrentUser)
    }

    override suspend fun updateCurrentUserSkills(updateRequest: UserSkillsUpdateRequest) {
        val currentUserId = userStorageDataSource.getCurrentUserId()
        val oldUserSkills = userStorageDataSource.getCurrentUser().userSkills
        val remoteCurrentUser = userRemoteDataSource.updateCurrentUserSkills(
            currentUserId,
            updateRequest,
            oldUserSkills
        )
        userStorageDataSource.saveCurrentUser(remoteCurrentUser)
    }

    override suspend fun updateCurrentUserExperience(updateRequest: UserExperienceUpdateRequest) {
        val currentUserId = userStorageDataSource.getCurrentUserId()
        val oldUserExperience = userStorageDataSource.getCurrentUser().userExperience
        val remoteCurrentUser = userRemoteDataSource.updateCurrentUserExperience(
            currentUserId,
            updateRequest,
            oldUserExperience
        )
        userStorageDataSource.saveCurrentUser(remoteCurrentUser)
    }

    override suspend fun updateCurrentUserEducation(updateRequest: UserEducationUpdateRequest) {
        val currentUserId = userStorageDataSource.getCurrentUserId()
        val oldUserEducation = userStorageDataSource.getCurrentUser().userEducation
        val remoteCurrentUser = userRemoteDataSource.updateCurrentUserEducation(
            currentUserId,
            updateRequest,
            oldUserEducation
        )
        userStorageDataSource.saveCurrentUser(remoteCurrentUser)
    }

    override suspend fun clearCache() {
        userStorageDataSource.clearCache()
    }
}