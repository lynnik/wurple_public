package com.wurple.data.model.mapper.experience

import android.content.Context
import com.wurple.R
import com.wurple.data.model.user.UserExperienceData
import com.wurple.domain.date.DateManager
import com.wurple.domain.model.date.Date
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.UserExperience

class FromUserExperienceDataToUserExperienceMapper(
    private val context: Context,
    private val dateManager: DateManager
) : Mapper<UserExperienceData, UserExperience> {
    override suspend fun map(from: UserExperienceData): UserExperience {
        val fromDateBaseDate = dateManager.convertStringDateToBaseDate(from.fromDate)
        val fromFormattedDate = dateManager.getMonthAndYearFormattedDate(fromDateBaseDate)
        val toDateBaseDate = dateManager.convertStringDateToBaseDate(from.toDate)
        val toFormattedDate = dateManager.getMonthAndYearFormattedDate(toDateBaseDate)
        val dateRange = dateManager.getDateDifference(
            fromDate = fromDateBaseDate,
            toDate = if (from.isCurrent) null else toDateBaseDate
        )
        return UserExperience(
            id = from.id,
            position = from.position,
            companyName = from.companyName,
            title = context.getString(
                R.string.common_experience_title,
                from.position,
                from.companyName
            ),
            isCurrent = from.isCurrent,
            fromDate = Date(
                baseDate = fromDateBaseDate,
                formattedDate = fromFormattedDate
            ),
            toDate = Date(
                baseDate = toDateBaseDate,
                formattedDate = toFormattedDate
            ),
            fromToFormattedDateRange = when {
                dateRange.years <= 0 && dateRange.months > 0 -> {
                    context.resources.getQuantityString(
                        R.plurals.experience_date_months,
                        dateRange.months,
                        dateRange.months
                    )
                }
                dateRange.years > 0 && dateRange.months <= 0 -> {
                    context.resources.getQuantityString(
                        R.plurals.experience_date_years,
                        dateRange.years,
                        dateRange.years
                    )
                }
                dateRange.years > 0 && dateRange.months > 0 -> {
                    context.getString(
                        R.string.experience_date_range_months_and_years,
                        context.resources.getQuantityString(
                            R.plurals.experience_date_years,
                            dateRange.years,
                            dateRange.years
                        ),
                        context.resources.getQuantityString(
                            R.plurals.experience_date_months,
                            dateRange.months,
                            dateRange.months
                        )
                    )
                }
                else -> {
                    null
                }
            }
        )
    }
}