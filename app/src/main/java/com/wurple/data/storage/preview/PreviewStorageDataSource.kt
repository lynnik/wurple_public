package com.wurple.data.storage.preview

import com.wurple.domain.model.preview.CreatePreviewRequest
import com.wurple.domain.model.preview.Preview
import kotlinx.coroutines.flow.Flow

interface PreviewStorageDataSource {
    suspend fun getDefaultPreview(currentUserId: String): Preview
    suspend fun savePreviews(previews: List<Preview>)
    fun observePreviews(): Flow<List<Preview>>
    suspend fun getTempPreview(): CreatePreviewRequest?
    suspend fun setTempPreview(createPreviewRequest: CreatePreviewRequest)
    suspend fun clearCache()
}