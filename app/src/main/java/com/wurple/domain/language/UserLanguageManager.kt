package com.wurple.domain.language

import com.wurple.domain.model.user.UserLanguage

interface UserLanguageManager {
    suspend fun getLanguages(json: String): List<UserLanguage>
    suspend fun getJsonOfLanguagesList(userLanguages: List<UserLanguage>): String
}