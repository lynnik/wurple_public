package com.wurple.data.storage.language

import com.wurple.domain.model.language.Language
import com.wurple.domain.model.language.LanguageLevel

interface LanguageStorageDataSource {
    suspend fun getLanguages(): List<Language>
    suspend fun getLanguageLevels(): List<LanguageLevel>
}