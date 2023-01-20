package com.wurple.data.storage.preview

import android.content.Context
import com.wurple.R
import com.wurple.data.model.mapper.preview.FromPreviewStorageDataToPreviewMapper
import com.wurple.data.model.mapper.preview.FromPreviewToPreviewStorageDataMapper
import com.wurple.data.storage.room.dao.PreviewDao
import com.wurple.domain.date.DateManager
import com.wurple.domain.extension.empty
import com.wurple.domain.extension.random
import com.wurple.domain.model.common.Properties
import com.wurple.domain.model.common.Status
import com.wurple.domain.model.common.StatusValue
import com.wurple.domain.model.date.Date
import com.wurple.domain.model.preview.CreatePreviewRequest
import com.wurple.domain.model.preview.Preview
import com.wurple.domain.model.preview.PreviewLifetimeOption
import com.wurple.domain.model.preview.PreviewVisibilityValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.util.*

class DefaultPreviewStorageDataSource(
    private val context: Context,
    private val previewDao: PreviewDao,
    private val fromPreviewToPreviewStorageDataMapper: FromPreviewToPreviewStorageDataMapper,
    private val fromPreviewStorageDataToPreviewMapper: FromPreviewStorageDataToPreviewMapper,
    private val dateManager: DateManager
) : PreviewStorageDataSource {
    private var tempPreview: CreatePreviewRequest? = null

    override suspend fun getDefaultPreview(currentUserId: String): Preview {
        val currentBaseDate = dateManager.getCurrentBaseDate()
        return Preview(
            id = UUID.randomUUID().toString(),
            title = context.getString(
                R.string.previews_add_title_auto_generated,
                String.random(4)
            ),
            previewLifetimeOption = PreviewLifetimeOption.Option3,
            // it could be any date, will be updated on preview creation
            expDate = Date(
                baseDate = currentBaseDate,
                formattedDate = dateManager.getDefaultFormattedDate(currentBaseDate)
            ),
            isExpired = false,
            userId = currentUserId,
            userImageVisibility = PreviewVisibilityValue.Hidden,
            userNameVisibility = PreviewVisibilityValue.Hidden,
            userCurrentPositionVisibility = PreviewVisibilityValue.PartiallyVisible,
            userLocationVisibility = PreviewVisibilityValue.PartiallyVisible,
            userEmailVisibility = PreviewVisibilityValue.Hidden,
            userUsernameVisibility = PreviewVisibilityValue.Hidden,
            userBioVisibility = PreviewVisibilityValue.Visible,
            userSkillsVisibility = PreviewVisibilityValue.Visible,
            userExperienceVisibility = PreviewVisibilityValue.PartiallyVisible,
            userEducationVisibility = PreviewVisibilityValue.PartiallyVisible,
            userLanguagesVisibility = PreviewVisibilityValue.Visible,
            properties = Properties(
                status = Status(
                    value = StatusValue.Enabled,
                    description = String.empty
                ),
                createdAt = Date(
                    baseDate = currentBaseDate,
                    formattedDate = dateManager.getDefaultFormattedDate(currentBaseDate)
                ),
                updatedAt = Date(
                    baseDate = currentBaseDate,
                    formattedDate = dateManager.getDefaultFormattedDate(currentBaseDate)
                )
            )
        )
    }

    override suspend fun savePreviews(previews: List<Preview>) {
        val data = previews.map { preview ->
            fromPreviewToPreviewStorageDataMapper.map(preview)
        }
        previewDao.deleteAllAndInsert(data)
    }

    override fun observePreviews(): Flow<List<Preview>> {
        return previewDao.observePreviews()
            .filterNotNull()
            .map { data ->
                data.map { previewStorageData ->
                    fromPreviewStorageDataToPreviewMapper.map(previewStorageData)
                }
            }
    }

    override suspend fun getTempPreview(): CreatePreviewRequest? = tempPreview

    override suspend fun setTempPreview(createPreviewRequest: CreatePreviewRequest) {
        tempPreview = createPreviewRequest
    }

    override suspend fun clearCache() {
        previewDao.clearCache()
    }
}