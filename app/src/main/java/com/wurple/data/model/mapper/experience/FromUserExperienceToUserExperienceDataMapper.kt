package com.wurple.data.model.mapper.experience

import com.wurple.data.model.user.UserExperienceData
import com.wurple.domain.date.DateManager
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.UserExperience

class FromUserExperienceToUserExperienceDataMapper(
    private val dateManager: DateManager,
) : Mapper<UserExperience, UserExperienceData> {
    override suspend fun map(from: UserExperience): UserExperienceData {
        return UserExperienceData(
            id = from.id,
            position = from.position,
            companyName = from.companyName,
            isCurrent = from.isCurrent,
            fromDate = dateManager.convertBaseDateToStringDate(from.fromDate.baseDate),
            toDate = dateManager.convertBaseDateToStringDate(from.toDate.baseDate)
        )
    }
}