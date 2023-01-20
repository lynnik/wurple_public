package com.wurple.data.storage.user

import android.content.Context
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.wurple.data.firebase.FirebaseManager
import com.wurple.data.model.mapper.user.FromUserStorageDataToUserMapper
import com.wurple.data.model.mapper.user.FromUserToUserStorageDataMapper
import com.wurple.data.storage.room.dao.UserDao
import com.wurple.domain.dispatcher.DispatcherProvider
import com.wurple.domain.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class DefaultUserStorageDataSource(
    private val context: Context,
    private val firebaseManager: FirebaseManager,
    private val userDao: UserDao,
    private val fromUserToUserStorageDataMapper: FromUserToUserStorageDataMapper,
    private val fromUserStorageDataToUserMapper: FromUserStorageDataToUserMapper,
    private val dispatcherProvider: DispatcherProvider
) : UserStorageDataSource {
    override fun getCurrentUserId(): String {
        val id = firebaseManager.currentUser.uid
        Firebase.crashlytics.setUserId(id)
        return id
    }

    override suspend fun saveCurrentUser(user: User) {
        val userStorageData = fromUserToUserStorageDataMapper.map(user)
        userDao.insert(userStorageData)
    }

    override suspend fun getCurrentUser(): User {
        val uid = getCurrentUserId()
        val userStorageData = userDao.getUserById(uid)
            ?: throw IllegalArgumentException("There is no user with id: $uid in storage")
        return fromUserStorageDataToUserMapper.map(userStorageData)
    }

    override fun observeCurrentUser(): Flow<User> {
        val uid = getCurrentUserId()
        return userDao.observeUserById(uid)
            .filterNotNull()
            .map { userStorageData -> fromUserStorageDataToUserMapper.map(userStorageData) }
    }

    override suspend fun getUserImageFile(): File {
        return withContext(dispatcherProvider.io) {
            File(
                context.cacheDir,
                "user_image_${System.currentTimeMillis()}.jpeg"
            )
        }
    }

    override suspend fun clearCache() {
        userDao.clearCache()
        withContext(dispatcherProvider.io) {
            context.cacheDir.deleteRecursively()
        }
    }
}