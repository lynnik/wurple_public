package com.wurple.domain.model.user

import com.wurple.domain.model.date.Date

data class UserExperience(
    val id: String,
    val position: String,
    val companyName: String,
    // title consists of position and companyName
    val title: String,
    val isCurrent: Boolean,
    val fromDate: Date,
    val toDate: Date,
    val fromToFormattedDateRange: String?
)