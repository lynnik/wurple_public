package com.wurple.data.storage.language

import com.wurple.data.language.languageLevels
import com.wurple.data.language.languages
import com.wurple.data.model.mapper.language.FromLanguageDataToLanguageMapper
import com.wurple.data.model.mapper.language.FromLanguageLevelDataToLanguageLevelMapper
import com.wurple.domain.model.language.Language
import com.wurple.domain.model.language.LanguageLevel

class DefaultLanguageStorageDataSource(
    private val fromLanguageDataToLanguageMapper: FromLanguageDataToLanguageMapper,
    private val fromLanguageLevelDataToLanguageLevelMapper: FromLanguageLevelDataToLanguageLevelMapper
) : LanguageStorageDataSource {
    override suspend fun getLanguages(): List<Language> {
        return languages.map { fromLanguageDataToLanguageMapper.map(it) }
    }

    override suspend fun getLanguageLevels(): List<LanguageLevel> {
        return languageLevels.map { fromLanguageLevelDataToLanguageLevelMapper.map(it) }
    }
}