package com.wurple.data.language

import com.wurple.R
import com.wurple.data.model.language.LanguageData

val languages: List<LanguageData>
    get() = listOf(
        ukrainian,
        english,
        chinese,
        spanish,
        arabic,
        japanese,
        hindustani,
        french,
        german,
        russian,
        portuguese,
        italian
    )

private val ukrainian = LanguageData(
    id = 0,
    nameStringResId = R.string.languages_language_ukrainian
)

private val english = LanguageData(
    id = 1,
    nameStringResId = R.string.languages_language_english
)

private val chinese = LanguageData(
    id = 2,
    nameStringResId = R.string.languages_language_chinese
)

private val spanish = LanguageData(
    id = 3,
    nameStringResId = R.string.languages_language_spanish
)

private val arabic = LanguageData(
    id = 4,
    nameStringResId = R.string.languages_language_arabic
)

private val japanese = LanguageData(
    id = 5,
    nameStringResId = R.string.languages_language_japanese
)

private val hindustani = LanguageData(
    id = 6,
    nameStringResId = R.string.languages_language_hindustani
)

private val french = LanguageData(
    id = 7,
    nameStringResId = R.string.languages_language_french
)

private val german = LanguageData(
    id = 8,
    nameStringResId = R.string.languages_language_german
)

private val russian = LanguageData(
    id = 9,
    nameStringResId = R.string.languages_language_russian
)

private val portuguese = LanguageData(
    id = 10,
    nameStringResId = R.string.languages_language_portuguese
)

private val italian = LanguageData(
    id = 11,
    nameStringResId = R.string.languages_language_italian
)