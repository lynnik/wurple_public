package com.wurple.data.model.mapper.education

import com.wurple.data.model.user.UserEducationData
import com.wurple.domain.date.DateManager
import com.wurple.domain.model.date.Date
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.UserEducation

class FromUserEducationDataToUserEducationMapper(
    private val dateManager: DateManager
) : Mapper<UserEducationData, UserEducation> {
    override suspend fun map(from: UserEducationData): UserEducation {
        val graduationDateBaseDate = dateManager.convertStringDateToBaseDate(from.graduationDate)
        return UserEducation(
            id = from.id,
            degree = from.degree,
            institution = from.institution,
            graduationDate = Date(
                baseDate = graduationDateBaseDate,
                formattedDate = dateManager.getMonthAndYearFormattedDate(graduationDateBaseDate)
            )
        )
    }
}