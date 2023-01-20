package com.wurple.domain.skills

import com.wurple.domain.model.user.UserSkill

interface UserSkillsManager {
    suspend fun getSkills(json: String): List<UserSkill>
    suspend fun getJsonOfSkillsList(userSkills: List<UserSkill>): String
}