package com.wurple.domain.gateway

interface InfoGateway {
    suspend fun shouldShowFillOutProfile(): Boolean
    suspend fun setFillOutProfileAsShown()
    suspend fun clearCache()
}
