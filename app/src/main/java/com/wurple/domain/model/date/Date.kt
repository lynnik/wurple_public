package com.wurple.domain.model.date

import java.time.ZonedDateTime

data class Date(
    val baseDate: ZonedDateTime,
    val formattedDate: String
)