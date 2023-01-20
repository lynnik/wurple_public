package com.wurple.data.remote.preview

import com.wurple.domain.model.preview.CreatePreviewRequest
import com.wurple.domain.model.preview.Preview

interface PreviewRemoteDataSource {
    suspend fun getPreviewById(id: String): Preview?
    suspend fun getPreviews(currentUserId: String): List<Preview>
    suspend fun createPreview(currentUserId: String, createPreviewRequest: CreatePreviewRequest)
    suspend fun updatePreview(id: String, updatePreviewRequest: CreatePreviewRequest)
    suspend fun deletePreview(id: String)
}