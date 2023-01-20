package com.wurple.data.remote.account

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wurple.data.firebase.FirebaseManager
import com.wurple.data.model.user.UserRemoteData
import com.wurple.domain.log.Logger
import kotlinx.coroutines.tasks.await

class DefaultAccountRemoteDataSource(
    private val firebaseManager: FirebaseManager,
    private val logger: Logger
) : AccountRemoteDataSource {
    override suspend fun deleteAccount(currentUserId: String) {
        // delete user image from Firebase Storage
        try {
            Firebase.storage.reference
                .child(UserRemoteData.generateImagePath(currentUserId))
                .delete()
                .await()
        } catch (throwable: Throwable) {
            logger.e(TAG, throwable)
        }
        // delete user from Firebase Firestore
        try {
            Firebase.firestore
                .collection(UserRemoteData.COLLECTION)
                .document(currentUserId)
                .delete()
                .await()
        } catch (throwable: Throwable) {
            logger.e(TAG, throwable)
        }
        // delete user from Firebase Auth
        firebaseManager.currentUser
            .delete()
            .await()
    }

    companion object {
        private const val TAG = "DefaultAccountRemoteDataSource"
    }
}