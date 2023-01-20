package com.wurple.domain.model.user

import com.wurple.domain.model.date.Date

data class UserDob(
    val date: Date,
    val age: Int
)