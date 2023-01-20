package com.wurple.data.model.mapper.language

import com.wurple.data.model.user.UserLanguageData
import com.wurple.domain.model.mapper.Mapper
import com.wurple.domain.model.user.UserLanguage

class FromUserLanguageToUserLanguageDataMapper : Mapper<UserLanguage, UserLanguageData> {
    override suspend fun map(from: UserLanguage): UserLanguageData {
        return UserLanguageData(
            languageId = from.language.id,
            languageLevelId = from.languageLevel.id
        )
    }
}