package com.wurple.data.experience

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.wurple.data.model.mapper.experience.FromUserExperienceDataToUserExperienceMapper
import com.wurple.data.model.mapper.experience.FromUserExperienceToUserExperienceDataMapper
import com.wurple.data.model.user.UserExperienceData
import com.wurple.domain.experience.UserExperienceManager
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.UserExperience

class DataUserExperienceManager(
    private val fromUserExperienceDataToUserExperienceMapper: FromUserExperienceDataToUserExperienceMapper,
    private val fromUserExperienceToUserExperienceDataMapper: FromUserExperienceToUserExperienceDataMapper,
    private val logger: Logger
) : UserExperienceManager {
    override suspend fun getExperience(json: String): List<UserExperience> {
        val userExperienceDataList = try {
            val typeOfUserExperienceData = object : TypeToken<List<UserExperienceData>>() {}.type
            Gson().fromJson<List<UserExperienceData>>(json, typeOfUserExperienceData) ?: listOf()
        } catch (exception: JsonSyntaxException) {
            logger.e(TAG, exception)
            listOf()
        }
        return userExperienceDataList.map { fromUserExperienceDataToUserExperienceMapper.map(it) }
    }

    override suspend fun getJsonOfExperienceList(userExperience: List<UserExperience>): String {
        val userExperienceDataList = userExperience.map { experience ->
            fromUserExperienceToUserExperienceDataMapper.map(experience)
        }
        return Gson().toJson(userExperienceDataList)
    }

    companion object {
        private const val TAG = "DataUserExperienceManager"
    }
}