package com.wurple.domain.model.user

data class UserEducationUpdateRequest(
    val id: String,
    val degree: String,
    val institution: String,
    val graduationDate: String,
    // set true if it should be deleted, also id must not be empty
    val isDelete: Boolean = false
)