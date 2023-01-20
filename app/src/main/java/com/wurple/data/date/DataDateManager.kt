package com.wurple.data.date

import com.wurple.domain.date.DateManager
import com.wurple.domain.log.Logger
import com.wurple.domain.model.date.DateRange
import com.wurple.domain.model.user.User
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class DataDateManager(
    private val logger: Logger
) : DateManager {
    override fun convertStringDateToBaseDate(date: String): ZonedDateTime {
        return ZonedDateTime.parse(date, isoFormatter())
    }

    override fun convertBaseDateToStringDate(date: ZonedDateTime): String {
        return date.format(isoFormatter())
    }

    override fun getCurrentStringDate(): String {
        val currentZonedDateTime = getCurrentBaseDate()
        return currentZonedDateTime.format(isoFormatter())
    }

    override fun getCurrentBaseDate(): ZonedDateTime = ZonedDateTime.now()

    override fun getCurrentStringDatePlusDays(days: Int): String {
        val zonedDateTime = getCurrentBaseDate().plusDays(days.toLong())
        return zonedDateTime.format(isoFormatter())
    }

    override fun getCurrentStringDatePlusMonths(months: Int): String {
        val zonedDateTime = getCurrentBaseDate().plusMonths(months.toLong())
        return zonedDateTime.format(isoFormatter())
    }

    override fun getDifferenceFromCurrentDateToDateInDays(toDate: ZonedDateTime): Int {
        val currentZonedDateTime = getCurrentBaseDate()
        return ChronoUnit.DAYS
            .between(currentZonedDateTime.toLocalDate(), toDate.toLocalDate())
            .toInt()
    }

    override fun getDateDifference(fromDate: ZonedDateTime, toDate: ZonedDateTime?): DateRange {
        val period = Period.between(
            fromDate.toLocalDate(),
            (toDate ?: getCurrentBaseDate()).toLocalDate()
        )
        return DateRange(
            days = period.days,
            months = period.months,
            years = period.years
        )
    }

    override fun getDefaultFormattedDate(date: ZonedDateTime): String {
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    override fun getMonthAndYearFormattedDate(date: ZonedDateTime): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_MONTH_AND_YEAR)
        return date.format(dateTimeFormatter)
    }

    override fun isDateValid(baseFormattedDateText: String): Boolean {
        return try {
            val yearMonth = YearMonth.parse(baseFormattedDateText, baseFormatter())
            yearMonth.atDay(1)
            true
        } catch (throwable: Throwable) {
            logger.e(TAG, throwable)
            false
        }
    }

    override fun isDateInTheFuture(baseFormattedDateText: String): Boolean {
        return if (isDateValid(baseFormattedDateText)) {
            val stringDate = defaultDateInputStringDateToStringDate(baseFormattedDateText)
            val baseDate = convertStringDateToBaseDate(stringDate)
            val currentBaseDate = getCurrentBaseDate()
            return baseDate.isAfter(currentBaseDate)
        } else {
            false
        }
    }

    override fun checkIsUserAgeValid(baseFormattedDateText: String): Boolean {
        return try {
            val localDatePlusUserMinAge = LocalDate.parse(baseFormattedDateText, baseFormatter())
                .plusYears(User.MIN_AGE.toLong())
                .minusDays(1)
            val localDatePlusUserMaxAge = LocalDate.parse(baseFormattedDateText, baseFormatter())
                .plusYears(User.MAX_AGE.toLong())
                .plusDays(1)
            val currentLocalDate = LocalDate.now()
            localDatePlusUserMinAge.isBefore(currentLocalDate) &&
                    localDatePlusUserMaxAge.isAfter(currentLocalDate)
        } catch (throwable: Throwable) {
            logger.e(TAG, throwable)
            false
        }
    }

    override fun getAgeFromBaseDate(date: ZonedDateTime): Int {
        return Period.between(date.toLocalDate(), LocalDate.now()).years
    }

    override fun baseDateToFormattedText(zonedDateTime: ZonedDateTime): String {
        return baseFormatter().format(zonedDateTime)
    }

    override fun defaultDateInputStringDateToStringDate(inputStringDate: String): String {
        val yearMonth = YearMonth.parse(inputStringDate, baseFormatter())
        val localDate = yearMonth.atDay(1)
        val zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault())
        return zonedDateTime.format(isoFormatter())
    }

    private fun isoFormatter(): DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    private fun baseFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_BASE)

    companion object {
        private const val TAG = "DataDateManager"
        private const val PATTERN_BASE = "MM/yyyy"
        private const val PATTERN_MONTH_AND_YEAR = "MMM, yyyy"
    }
}