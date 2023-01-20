package com.wurple.data.storage.info

interface InfoStorageDataSource {
    suspend fun shouldShowFillOutProfile(): Boolean
    suspend fun setFillOutProfileAsShown()
    suspend fun clearCache()
}
