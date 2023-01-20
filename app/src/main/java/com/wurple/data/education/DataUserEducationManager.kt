package com.wurple.data.education

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.wurple.data.model.mapper.education.FromUserEducationDataToUserEducationMapper
import com.wurple.data.model.mapper.education.FromUserEducationToUserEducationDataMapper
import com.wurple.data.model.user.UserEducationData
import com.wurple.domain.education.UserEducationManager
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.UserEducation

class DataUserEducationManager(
    private val fromUserEducationDataToUserEducationMapper: FromUserEducationDataToUserEducationMapper,
    private val fromUserEducationToUserEducationDataMapper: FromUserEducationToUserEducationDataMapper,
    private val logger: Logger
) : UserEducationManager {
    override suspend fun getEducation(json: String): List<UserEducation> {
        val userEducationDataList = try {
            val typeOfUserEducationData = object : TypeToken<List<UserEducationData>>() {}.type
            Gson().fromJson<List<UserEducationData>>(json, typeOfUserEducationData) ?: listOf()
        } catch (exception: JsonSyntaxException) {
            logger.e(TAG, exception)
            listOf()
        }
        return userEducationDataList.map { fromUserEducationDataToUserEducationMapper.map(it) }
    }

    override suspend fun getJsonOfEducationList(userEducation: List<UserEducation>): String {
        val userEducationDataList = userEducation.map { education ->
            fromUserEducationToUserEducationDataMapper.map(education)
        }
        return Gson().toJson(userEducationDataList)
    }

    companion object {
        private const val TAG = "DataUserEducationManager"
    }
}