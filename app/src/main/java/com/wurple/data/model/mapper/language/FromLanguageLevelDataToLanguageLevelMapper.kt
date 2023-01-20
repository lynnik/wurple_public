package com.wurple.data.model.mapper.language

import android.content.Context
import com.wurple.data.model.language.LanguageLevelData
import com.wurple.domain.model.language.LanguageLevel
import com.wurple.domain.model.mapper.Mapper

class FromLanguageLevelDataToLanguageLevelMapper(
    private val context: Context
) : Mapper<LanguageLevelData, LanguageLevel> {
    override suspend fun map(from: LanguageLevelData): LanguageLevel {
        return LanguageLevel(
            id = from.id,
            name = context.getString(from.nameStringResId)
        )
    }
}