package com.wurple.data.language

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.wurple.data.model.mapper.language.FromLanguageDataToLanguageMapper
import com.wurple.data.model.mapper.language.FromLanguageLevelDataToLanguageLevelMapper
import com.wurple.data.model.mapper.language.FromUserLanguageToUserLanguageDataMapper
import com.wurple.data.model.user.UserLanguageData
import com.wurple.domain.language.UserLanguageManager
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.UserLanguage

class DataUserLanguageManager(
    private val fromLanguageDataToLanguageMapper: FromLanguageDataToLanguageMapper,
    private val fromLanguageLevelDataToLanguageLevelMapper: FromLanguageLevelDataToLanguageLevelMapper,
    private val fromUserLanguageToUserLanguageDataMapper: FromUserLanguageToUserLanguageDataMapper,
    private val logger: Logger
) : UserLanguageManager {
    override suspend fun getLanguages(json: String): List<UserLanguage> {
        val userLanguageDataList = try {
            val typeOfUserLanguageData = object : TypeToken<List<UserLanguageData>>() {}.type
            Gson().fromJson<List<UserLanguageData>>(json, typeOfUserLanguageData) ?: listOf()
        } catch (exception: JsonSyntaxException) {
            logger.e(TAG, exception)
            listOf()
        }
        return userLanguageDataList
            .mapNotNull { userLanguageData ->
                val languageData = languages.find { it.id == userLanguageData.languageId }
                val languageLevelData =
                    languageLevels.find { it.id == userLanguageData.languageLevelId }
                if (languageData != null && languageLevelData != null) {
                    val language = fromLanguageDataToLanguageMapper.map(languageData)
                    val languageLevel =
                        fromLanguageLevelDataToLanguageLevelMapper.map(languageLevelData)
                    UserLanguage(
                        language = language,
                        languageLevel = languageLevel
                    )
                } else {
                    null
                }
            }
    }

    override suspend fun getJsonOfLanguagesList(userLanguages: List<UserLanguage>): String {
        val userLanguageDataList = userLanguages.map { userLanguage ->
            fromUserLanguageToUserLanguageDataMapper.map(userLanguage)
        }
        return Gson().toJson(userLanguageDataList)
    }

    companion object {
        private const val TAG = "DataUserLanguageManager"
    }
}