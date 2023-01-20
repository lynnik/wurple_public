package com.wurple.data.skills

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.wurple.data.model.mapper.skills.FromUserSkillsDataToUserSkillsMapper
import com.wurple.data.model.mapper.skills.FromUserSkillsToUserSkillsDataMapper
import com.wurple.data.model.user.UserSkillData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.UserSkill
import com.wurple.domain.skills.UserSkillsManager

class DataUserSkillsManager(
    private val fromUserSkillsDataToUserSkillsMapper: FromUserSkillsDataToUserSkillsMapper,
    private val fromUserSkillsToUserSkillsDataMapper: FromUserSkillsToUserSkillsDataMapper,
    private val logger: Logger
) : UserSkillsManager {
    override suspend fun getSkills(json: String): List<UserSkill> {
        val userSkillsDataList = try {
            val typeOfUserSkillsData = object : TypeToken<List<UserSkillData>>() {}.type
            Gson().fromJson<List<UserSkillData>>(json, typeOfUserSkillsData) ?: listOf()
        } catch (exception: JsonSyntaxException) {
            logger.e(TAG, exception)
            listOf()
        }
        return userSkillsDataList.map { fromUserSkillsDataToUserSkillsMapper.map(it) }
    }

    override suspend fun getJsonOfSkillsList(userSkills: List<UserSkill>): String {
        val userSkillsDataList = userSkills.map { skill ->
            fromUserSkillsToUserSkillsDataMapper.map(skill)
        }
        return Gson().toJson(userSkillsDataList)
    }

    companion object {
        private const val TAG = "DataUserSkillsManager"
    }
}