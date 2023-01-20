package com.wurple.domain.date

import com.wurple.domain.model.date.DateRange
import java.time.ZonedDateTime

interface DateManager {
    /**
     * Convert string date to appropriate format.
     * @param date string, kind of: 2021-12-27T19:01:57.145Z
     * @return [ZonedDateTime] object
     */
    fun convertStringDateToBaseDate(date: String): ZonedDateTime

    /**
     * Convert base date to string.
     * @param date [ZonedDateTime] object
     * @return string value: 2021-12-27T19:01:57.145Z
     */
    fun convertBaseDateToStringDate(date: ZonedDateTime): String

    /**
     * Returns current date.
     * @return string value: 2021-12-27T19:01:57.145Z
     */
    fun getCurrentStringDate(): String

    /**
     * Returns current base date.
     * @return [ZonedDateTime] object
     */
    fun getCurrentBaseDate(): ZonedDateTime

    /**
     * Returns current date plus days.
     * @param days number of days to add to current date
     * @return string value: 2021-12-27T19:01:57.145Z
     */
    fun getCurrentStringDatePlusDays(days: Int): String

    /**
     * Returns current date plus months.
     * @param months number of days to add to current date
     * @return string value: 2021-12-27T19:01:57.145Z
     */
    fun getCurrentStringDatePlusMonths(months: Int): String

    /**
     * Returns a difference between current date and future date in days.
     * @param toDate a future date
     * @return number of days
     */
    fun getDifferenceFromCurrentDateToDateInDays(toDate: ZonedDateTime): Int

    /**
     * Returns a range between fromDate and toDate.
     * @param fromDate
     * @param toDate if null - takes current date
     * @return [DateRange]
     */
    fun getDateDifference(fromDate: ZonedDateTime, toDate: ZonedDateTime?): DateRange

    /**
     * Format a date to readable format.
     * @param date [ZonedDateTime] object
     * @return formatted string: Dec 2, 1993
     */
    fun getDefaultFormattedDate(date: ZonedDateTime): String

    /**
     * Format a date to readable format.
     * @param date [ZonedDateTime] object
     * @return formatted string: Dec, 1993
     */
    fun getMonthAndYearFormattedDate(date: ZonedDateTime): String

    /**
     * Check is the formatted text valid date.
     * @param baseFormattedDateText user input, kind of: 12/1993
     * @return true if it's valid else false
     */
    fun isDateValid(baseFormattedDateText: String): Boolean

    /**
     * Check is the formatted text is a date in the future.
     * @param baseFormattedDateText user input, kind of: 12/1993
     * @return true if it's in the future else false
     */
    fun isDateInTheFuture(baseFormattedDateText: String): Boolean

    /**
     * Check is user age between min and max age.
     * @param baseFormattedDateText user input, kind of: 12/1993
     * @return true if it's valid age else false
     */
    fun checkIsUserAgeValid(baseFormattedDateText: String): Boolean

    /**
     * Get an age from date.
     * @param date object
     * @return age: 28
     */
    fun getAgeFromBaseDate(date: ZonedDateTime): Int

    /**
     * Format user's date to appropriate format.
     * @param zonedDateTime user's date of birthday
     * @return formatted date string: 12/1993
     */
    fun baseDateToFormattedText(zonedDateTime: ZonedDateTime): String

    /**
     * Format user's input formatted date of birthday text to appropriate string date.
     * @param inputStringDate user's formatted date of birthday text, kind of: 12/02/1993
     * @return date string value: 1993-12-02T00:00:00.000Z
     */
    fun defaultDateInputStringDateToStringDate(inputStringDate: String): String
}