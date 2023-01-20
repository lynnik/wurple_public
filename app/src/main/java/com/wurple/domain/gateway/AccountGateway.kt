package com.wurple.domain.gateway

interface AccountGateway {
    suspend fun deleteAccount(currentUserId: String)
}