package com.wurple.domain.gateway

import com.wurple.domain.model.preview.CreatePreviewRequest
import com.wurple.domain.model.preview.Preview
import kotlinx.coroutines.flow.Flow

interface PreviewGateway {
    suspend fun getPreviewById(id: String): Preview?
    suspend fun getDefaultPreview(currentUserId: String): Preview
    suspend fun fetchPreviews(currentUserId: String)
    fun observePreviews(): Flow<List<Preview>>
    suspend fun deletePreview(currentUserId: String, id: String)
    suspend fun getTempPreview(): CreatePreviewRequest?
    suspend fun setTempPreview(createPreviewRequest: CreatePreviewRequest)
    suspend fun createPreview(currentUserId: String, createPreviewRequest: CreatePreviewRequest)
    suspend fun updatePreview(
        currentUserId: String,
        id: String,
        createPreviewRequest: CreatePreviewRequest
    )

    suspend fun clearCache()
}
