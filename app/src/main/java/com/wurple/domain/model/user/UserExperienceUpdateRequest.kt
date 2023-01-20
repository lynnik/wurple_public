package com.wurple.domain.model.user

data class UserExperienceUpdateRequest(
    val id: String,
    val position: String,
    val companyName: String,
    val isCurrent: Boolean,
    val fromDate: String,
    val toDate: String,
    // set true if it should be deleted, also id must not be empty
    val isDelete: Boolean = false
)