package com.wurple.domain.model.user

data class UserSkill(
    val id: String,
    val title: String,
    val type: Type
) {
    sealed class Type(val value: Int) {
        object Personal : Type(0)
        object Professional : Type(1)

        companion object {
            fun getByValue(value: Int): Type {
                return when (value) {
                    Personal.value -> Personal
                    Professional.value -> Professional
                    else -> throw IllegalArgumentException(
                        "There is no UserSkillType with value: $value"
                    )
                }
            }
        }
    }
}