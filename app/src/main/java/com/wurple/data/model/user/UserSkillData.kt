package com.wurple.data.model.user

import com.google.gson.annotations.SerializedName

data class UserSkillData(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: Int
)