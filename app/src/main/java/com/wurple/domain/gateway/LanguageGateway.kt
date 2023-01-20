package com.wurple.domain.gateway

import com.wurple.domain.model.language.Language
import com.wurple.domain.model.language.LanguageLevel

interface LanguageGateway {
    suspend fun getLanguages(): List<Language>
    suspend fun getLanguageLevels(): List<LanguageLevel>
}