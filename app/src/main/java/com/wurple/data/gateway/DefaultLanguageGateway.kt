package com.wurple.data.gateway

import com.wurple.data.storage.language.LanguageStorageDataSource
import com.wurple.domain.gateway.LanguageGateway
import com.wurple.domain.model.language.Language
import com.wurple.domain.model.language.LanguageLevel

class DefaultLanguageGateway(
    private val languageStorageDataSource: LanguageStorageDataSource
) : LanguageGateway {
    override suspend fun getLanguages(): List<Language> {
        return languageStorageDataSource.getLanguages()
    }

    override suspend fun getLanguageLevels(): List<LanguageLevel> {
        return languageStorageDataSource.getLanguageLevels()
    }
}