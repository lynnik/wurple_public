package com.wurple

import com.wurple.data.date.DataDateManager
import com.wurple.data.log.DefaultLogger
import com.wurple.domain.date.DateManager
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DateManagerUnitTest {
    private val dateManager: DateManager = DataDateManager(DefaultLogger())
    private val isoDate = "1993-12-01T00:00:00+02:00"
    private val baseFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @Test
    fun convertStringDateToBaseDate_isCorrect() {
        val zonedDateTime = dateManager.convertStringDateToBaseDate(isoDate)
        assertEquals(isoDate, zonedDateTime.format(baseFormatter))
    }

    @Test
    fun convertBaseDateToStringDate_isCorrect() {
        val zonedDateTime = ZonedDateTime.parse(isoDate, baseFormatter)
        assertEquals(isoDate, dateManager.convertBaseDateToStringDate(zonedDateTime))
    }

    @Test
    fun getCurrentStringDate_isCorrect() {
        val currentStringDateExpected = ZonedDateTime.now().format(baseFormatter)
        val currentStringDateActual = dateManager.getCurrentStringDate()
        val zonedDateTimeExpected = ZonedDateTime.parse(currentStringDateExpected, baseFormatter)
        val zonedDateTimeActual = ZonedDateTime.parse(currentStringDateActual, baseFormatter)
        assertEquals(zonedDateTimeExpected.year, zonedDateTimeActual.year)
        assertEquals(zonedDateTimeExpected.month, zonedDateTimeActual.month)
        assertEquals(zonedDateTimeExpected.dayOfMonth, zonedDateTimeActual.dayOfMonth)
        assertEquals(zonedDateTimeExpected.hour, zonedDateTimeActual.hour)
        assertEquals(zonedDateTimeExpected.minute, zonedDateTimeActual.minute)
        assertEquals(zonedDateTimeExpected.second, zonedDateTimeActual.second)
        assertEquals(zonedDateTimeExpected.zone.id, zonedDateTimeActual.zone.id)
    }

    @Test
    fun getCurrentBaseDate_isCorrect() {
        val zonedDateTimeExpected = ZonedDateTime.now()
        val zonedDateTimeActual = dateManager.getCurrentBaseDate()
        assertEquals(zonedDateTimeExpected.year, zonedDateTimeActual.year)
        assertEquals(zonedDateTimeExpected.month, zonedDateTimeActual.month)
        assertEquals(zonedDateTimeExpected.dayOfMonth, zonedDateTimeActual.dayOfMonth)
        assertEquals(zonedDateTimeExpected.hour, zonedDateTimeActual.hour)
        assertEquals(zonedDateTimeExpected.minute, zonedDateTimeActual.minute)
        assertEquals(zonedDateTimeExpected.second, zonedDateTimeActual.second)
        assertEquals(zonedDateTimeExpected.zone.id, zonedDateTimeActual.zone.id)
    }

    @Test
    fun getCurrentStringDatePlusDays_isCorrect() {
        val currentStringDateExpected = ZonedDateTime.now().plusDays(5).format(baseFormatter)
        val currentStringDateActual = dateManager.getCurrentStringDatePlusDays(5)
        val zonedDateTimeExpected = ZonedDateTime.parse(currentStringDateExpected, baseFormatter)
        val zonedDateTimeActual = ZonedDateTime.parse(currentStringDateActual, baseFormatter)
        assertEquals(zonedDateTimeExpected.year, zonedDateTimeActual.year)
        assertEquals(zonedDateTimeExpected.month, zonedDateTimeActual.month)
        assertEquals(zonedDateTimeExpected.dayOfMonth, zonedDateTimeActual.dayOfMonth)
        assertEquals(zonedDateTimeExpected.hour, zonedDateTimeActual.hour)
        assertEquals(zonedDateTimeExpected.minute, zonedDateTimeActual.minute)
        assertEquals(zonedDateTimeExpected.second, zonedDateTimeActual.second)
        assertEquals(zonedDateTimeExpected.zone.id, zonedDateTimeActual.zone.id)
    }

    @Test
    fun getCurrentStringDatePlusMonths_isCorrect() {
        val currentStringDateExpected = ZonedDateTime.now().plusMonths(3).format(baseFormatter)
        val currentStringDateActual = dateManager.getCurrentStringDatePlusMonths(3)
        val zonedDateTimeExpected = ZonedDateTime.parse(currentStringDateExpected, baseFormatter)
        val zonedDateTimeActual = ZonedDateTime.parse(currentStringDateActual, baseFormatter)
        assertEquals(zonedDateTimeExpected.year, zonedDateTimeActual.year)
        assertEquals(zonedDateTimeExpected.month, zonedDateTimeActual.month)
        assertEquals(zonedDateTimeExpected.dayOfMonth, zonedDateTimeActual.dayOfMonth)
        assertEquals(zonedDateTimeExpected.hour, zonedDateTimeActual.hour)
        assertEquals(zonedDateTimeExpected.minute, zonedDateTimeActual.minute)
        assertEquals(zonedDateTimeExpected.second, zonedDateTimeActual.second)
        assertEquals(zonedDateTimeExpected.zone.id, zonedDateTimeActual.zone.id)
    }

    @Test
    fun getDifferenceFromCurrentDateToDateInDays_isCorrect() {
        val futureBaseDate = ZonedDateTime.now().plusDays(120)
        val dateDifferenceExpected = ChronoUnit.DAYS.between(
            dateManager.getCurrentBaseDate().toLocalDate(),
            futureBaseDate.toLocalDate()
        ).toInt()
        val dateDifferenceActual =
            dateManager.getDifferenceFromCurrentDateToDateInDays(futureBaseDate)
        assertEquals(dateDifferenceExpected, dateDifferenceActual)
    }

    @Test
    fun getDefaultFormattedDate_isCorrect() {
        val zonedDateTime = ZonedDateTime.parse(isoDate, baseFormatter)
        assertEquals("Dec 1, 1993", dateManager.getDefaultFormattedDate(zonedDateTime))
    }

    @Test
    fun getMonthAndYearFormattedDate_isCorrect() {
        val zonedDateTime = ZonedDateTime.parse(isoDate, baseFormatter)
        assertEquals("Dec, 1993", dateManager.getMonthAndYearFormattedDate(zonedDateTime))
    }

    @Test
    fun checkIsUserAgeValid_isCorrect() {
        assertEquals(true, dateManager.checkIsUserAgeValid("12/1993"))
    }

    @Test
    fun checkIsUserAgeValid_isIncorrect() {
        assertEquals(false, dateManager.checkIsUserAgeValid("12/1900"))
        assertEquals(false, dateManager.checkIsUserAgeValid("12/2050"))
    }

    @Test
    fun getAgeFromBaseDate_isCorrect() {
        val zonedDateTime = ZonedDateTime.parse(isoDate, baseFormatter)
        assertEquals(28, dateManager.getAgeFromBaseDate(zonedDateTime))
    }

    @Test
    fun baseDateToFormattedText_isCorrect() {
        val zonedDateTime = ZonedDateTime.parse(isoDate, baseFormatter)
        assertEquals("12/1993", dateManager.baseDateToFormattedText(zonedDateTime))
    }

    @Test
    fun defaultDateInputStringDateToStringDate_isCorrect() {
        assertEquals(isoDate, dateManager.defaultDateInputStringDateToStringDate("12/1993"))
    }
}