package com.wurple.data.model.mapper.language

import android.content.Context
import com.wurple.data.model.language.LanguageData
import com.wurple.domain.model.language.Language
import com.wurple.domain.model.mapper.Mapper

class FromLanguageDataToLanguageMapper(
    private val context: Context
) : Mapper<LanguageData, Language> {
    override suspend fun map(from: LanguageData): Language {
        return Language(
            id = from.id,
            name = context.getString(from.nameStringResId)
        )
    }
}