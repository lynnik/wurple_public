package com.wurple.data.model.mapper.preview

import com.wurple.data.model.preview.PreviewStorageData
import com.wurple.domain.date.DateManager
import com.wurple.domain.model.common.Properties
import com.wurple.domain.model.common.Status
import com.wurple.domain.model.common.StatusValue
import com.wurple.domain.model.date.Date
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.preview.Preview
import com.wurple.domain.model.preview.PreviewLifetimeOption
import com.wurple.domain.model.preview.PreviewVisibilityValue

class FromPreviewStorageDataToPreviewMapper(
    private val dateManager: DateManager
) : Mapper<PreviewStorageData, Preview> {
    override suspend fun map(from: PreviewStorageData): Preview {
        val expDateBaseDate = dateManager.convertStringDateToBaseDate(from.expDate)
        val createdAtBaseDate = dateManager.convertStringDateToBaseDate(from.createdAt)
        val updatedAtBaseDate = dateManager.convertStringDateToBaseDate(from.updatedAt)
        return Preview(
            id = from.id,
            title = from.title,
            previewLifetimeOption = PreviewLifetimeOption.getByValue(from.previewLifetimeOption),
            expDate = Date(
                baseDate = expDateBaseDate,
                formattedDate = dateManager.getDefaultFormattedDate(expDateBaseDate)
            ),
            isExpired = from.isExpired,
            userId = from.userId,
            // visibility values
            userImageVisibility = PreviewVisibilityValue.getByValue(from.userImageVisibility),
            userNameVisibility = PreviewVisibilityValue.getByValue(from.userNameVisibility),
            userCurrentPositionVisibility = PreviewVisibilityValue.getByValue(from.userCurrentPositionVisibility),
            userLocationVisibility = PreviewVisibilityValue.getByValue(from.userLocationVisibility),
            userEmailVisibility = PreviewVisibilityValue.getByValue(from.userEmailVisibility),
            userUsernameVisibility = PreviewVisibilityValue.getByValue(from.userUsernameVisibility),
            userBioVisibility = PreviewVisibilityValue.getByValue(from.userBioVisibility),
            userSkillsVisibility = PreviewVisibilityValue.getByValue(from.userSkillsVisibility),
            userExperienceVisibility = PreviewVisibilityValue.getByValue(from.userExperienceVisibility),
            userEducationVisibility = PreviewVisibilityValue.getByValue(from.userEducationVisibility),
            userLanguagesVisibility = PreviewVisibilityValue.getByValue(from.userLanguagesVisibility),
            properties = Properties(
                status = Status(
                    value = StatusValue.getByValue(from.statusValue),
                    description = from.statusDescription
                ),
                createdAt = Date(
                    baseDate = createdAtBaseDate,
                    formattedDate = dateManager.getDefaultFormattedDate(createdAtBaseDate)
                ),
                updatedAt = Date(
                    baseDate = updatedAtBaseDate,
                    formattedDate = dateManager.getDefaultFormattedDate(updatedAtBaseDate)
                )
            )
        )
    }
}