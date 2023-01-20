package com.wurple.data.model.mapper.skills

import com.wurple.data.model.user.UserSkillData
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.UserSkill

class FromUserSkillsToUserSkillsDataMapper : Mapper<UserSkill, UserSkillData> {
    override suspend fun map(from: UserSkill): UserSkillData {
        return UserSkillData(
            id = from.id,
            title = from.title,
            type = from.type.value
        )
    }
}