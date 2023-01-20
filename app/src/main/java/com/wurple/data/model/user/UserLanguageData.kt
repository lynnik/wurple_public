package com.wurple.data.model.user

import com.google.gson.annotations.SerializedName

data class UserLanguageData(
    @SerializedName("languageId") val languageId: Int,
    @SerializedName("languageLevelId") val languageLevelId: Int
)