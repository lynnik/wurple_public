package com.wurple.data.language

import com.wurple.R
import com.wurple.data.model.language.LanguageLevelData

val languageLevels: List<LanguageLevelData>
    get() = listOf(
        level0,
        level1,
        level2,
        level3,
        level4,
        level5
    )

private val level0 = LanguageLevelData(
    id = 0,
    nameStringResId = R.string.languages_level_0
)

private val level1 = LanguageLevelData(
    id = 1,
    nameStringResId = R.string.languages_level_1
)

private val level2 = LanguageLevelData(
    id = 2,
    nameStringResId = R.string.languages_level_2
)

private val level3 = LanguageLevelData(
    id = 3,
    nameStringResId = R.string.languages_level_3
)

private val level4 = LanguageLevelData(
    id = 4,
    nameStringResId = R.string.languages_level_4
)

private val level5 = LanguageLevelData(
    id = 5,
    nameStringResId = R.string.languages_level_5
)