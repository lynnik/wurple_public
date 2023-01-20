package com.wurple.domain.model.user

import com.wurple.domain.model.date.Date

data class UserEducation(
    val id: String,
    val degree: String,
    val institution: String,
    val graduationDate: Date
)