package com.wurple.data.model.mapper.skills

import com.wurple.data.model.user.UserSkillData
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.UserSkill

class FromUserSkillsDataToUserSkillsMapper : Mapper<UserSkillData, UserSkill> {
    override suspend fun map(from: UserSkillData): UserSkill {
        return UserSkill(
            id = from.id,
            title = from.title,
            type = UserSkill.Type.getByValue(from.type)
        )
    }
}