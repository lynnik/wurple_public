package com.wurple.domain.education

import com.wurple.domain.model.user.UserEducation

interface UserEducationManager {
    suspend fun getEducation(json: String): List<UserEducation>
    suspend fun getJsonOfEducationList(userEducation: List<UserEducation>): String
}