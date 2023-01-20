package com.wurple.data.storage.user

import com.wurple.domain.model.user.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserStorageDataSource {
    // it shouldn't be suspend to have an ability to use with Flow
    fun getCurrentUserId(): String
    suspend fun saveCurrentUser(user: User)
    suspend fun getCurrentUser(): User
    fun observeCurrentUser(): Flow<User>
    suspend fun getUserImageFile(): File
    suspend fun clearCache()
}