package com.wurple.domain.model.user

data class UserSkillsUpdateRequest(
    val userSkills: List<UserSkill>,
    // set true if it should be deleted, also id must not be empty
    val isDelete: Boolean = false
)