package com.wurple.data.gateway

import com.wurple.data.remote.preview.PreviewRemoteDataSource
import com.wurple.data.storage.preview.PreviewStorageDataSource
import com.wurple.domain.gateway.PreviewGateway
import com.wurple.domain.model.preview.CreatePreviewRequest
import com.wurple.domain.model.preview.Preview
import kotlinx.coroutines.flow.Flow

class DefaultPreviewGateway(
    private val previewStorageDataSource: PreviewStorageDataSource,
    private val previewRemoteDataSource: PreviewRemoteDataSource
) : PreviewGateway {
    override suspend fun getPreviewById(id: String): Preview? {
        return previewRemoteDataSource.getPreviewById(id)
    }

    override suspend fun getDefaultPreview(currentUserId: String): Preview {
        return previewStorageDataSource.getDefaultPreview(currentUserId)
    }

    override suspend fun fetchPreviews(currentUserId: String) {
        val previews = previewRemoteDataSource.getPreviews(currentUserId)
        previewStorageDataSource.savePreviews(previews)
    }

    override suspend fun deletePreview(currentUserId: String, id: String) {
        previewRemoteDataSource.deletePreview(id)
        fetchPreviews(currentUserId)
    }

    override fun observePreviews(): Flow<List<Preview>> {
        return previewStorageDataSource.observePreviews()
    }

    override suspend fun getTempPreview(): CreatePreviewRequest? {
        return previewStorageDataSource.getTempPreview()
    }

    override suspend fun setTempPreview(createPreviewRequest: CreatePreviewRequest) {
        previewStorageDataSource.setTempPreview(createPreviewRequest)
    }

    override suspend fun createPreview(
        currentUserId: String,
        createPreviewRequest: CreatePreviewRequest
    ) {
        previewRemoteDataSource.createPreview(currentUserId, createPreviewRequest)
        fetchPreviews(currentUserId)
    }

    override suspend fun updatePreview(
        currentUserId: String,
        id: String,
        createPreviewRequest: CreatePreviewRequest
    ) {
        previewRemoteDataSource.updatePreview(id, createPreviewRequest)
        fetchPreviews(currentUserId)
    }

    override suspend fun clearCache() {
        previewStorageDataSource.clearCache()
    }
}
