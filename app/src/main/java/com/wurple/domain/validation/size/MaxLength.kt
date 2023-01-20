package com.wurple.domain.validation.size

object MaxLength {
    const val EMAIL = 256

    const val USER_FIRST_NAME = 64
    const val USER_LAST_NAME = 64
    const val USER_USERNAME = 32
    const val USER_BIO = 128
    const val USER_SKILL = 24
    const val USER_SKILLS = 512
    const val USER_POSITION = 64
    const val USER_DEGREE = 64
    const val USER_INSTITUTION = 128
    const val USER_PERSONAL_SKILL_LIST_SIZE = 8
    const val USER_PROFESSIONAL_SKILL_LIST_SIZE = 16
    const val USER_EXPERIENCE_LIST_SIZE = 10
    const val USER_EDUCATION_LIST_SIZE = 10
    const val USER_LANGUAGE_LIST_SIZE = 10

    const val COMPANY_NAME = 64

    const val DEFAULT_DATE = 7 // including special symbols - 02/1993
}