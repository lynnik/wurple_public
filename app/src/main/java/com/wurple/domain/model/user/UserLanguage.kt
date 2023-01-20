package com.wurple.domain.model.user

import com.wurple.domain.model.language.Language
import com.wurple.domain.model.language.LanguageLevel

data class UserLanguage(
    val language: Language,
    val languageLevel: LanguageLevel
)