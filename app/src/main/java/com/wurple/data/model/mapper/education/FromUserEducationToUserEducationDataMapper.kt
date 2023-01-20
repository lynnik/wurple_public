package com.wurple.data.model.mapper.education

import com.wurple.data.model.user.UserEducationData
import com.wurple.domain.date.DateManager
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.UserEducation

class FromUserEducationToUserEducationDataMapper(
    private val dateManager: DateManager,
) : Mapper<UserEducation, UserEducationData> {
    override suspend fun map(from: UserEducation): UserEducationData {
        return UserEducationData(
            id = from.id,
            degree = from.degree,
            institution = from.institution,
            graduationDate = dateManager.convertBaseDateToStringDate(from.graduationDate.baseDate)
        )
    }
}