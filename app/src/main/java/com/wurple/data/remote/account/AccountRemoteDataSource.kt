package com.wurple.data.remote.account

interface AccountRemoteDataSource {
    suspend fun deleteAccount(currentUserId: String)
}