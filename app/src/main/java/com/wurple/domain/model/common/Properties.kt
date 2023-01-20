package com.wurple.domain.model.common

import com.wurple.domain.model.date.Date

data class Properties(
    val status: Status,
    val createdAt: Date,
    val updatedAt: Date
)