package com.wurple.data.model.mapper.preview

import com.wurple.data.model.preview.PreviewStorageData
import com.wurple.domain.date.DateManager
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.preview.Preview

class FromPreviewToPreviewStorageDataMapper(
    private val dateManager: DateManager
) : Mapper<Preview, PreviewStorageData> {
    override suspend fun map(from: Preview): PreviewStorageData {
        val expDate = dateManager.convertBaseDateToStringDate(from.expDate.baseDate)
        return PreviewStorageData(
            id = from.id,
            title = from.title,
            previewLifetimeOption = from.previewLifetimeOption.value,
            expDate = expDate,
            isExpired = from.isExpired,
            userId = from.userId,
            // visibility values
            userImageVisibility = from.userImageVisibility.value,
            userNameVisibility = from.userNameVisibility.value,
            userCurrentPositionVisibility = from.userCurrentPositionVisibility.value,
            userLocationVisibility = from.userLocationVisibility.value,
            userEmailVisibility = from.userEmailVisibility.value,
            userUsernameVisibility = from.userUsernameVisibility.value,
            userBioVisibility = from.userBioVisibility.value,
            userSkillsVisibility = from.userSkillsVisibility.value,
            userExperienceVisibility = from.userExperienceVisibility.value,
            userEducationVisibility = from.userEducationVisibility.value,
            userLanguagesVisibility = from.userLanguagesVisibility.value,
            // properties
            statusValue = from.properties.status.value.value,
            statusDescription = from.properties.status.description,
            createdAt = dateManager.convertBaseDateToStringDate(from.properties.createdAt.baseDate),
            updatedAt = dateManager.convertBaseDateToStringDate(from.properties.updatedAt.baseDate)
        )
    }
}