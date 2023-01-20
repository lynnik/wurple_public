package com.wurple.data.model.user

import com.google.gson.annotations.SerializedName

data class UserEducationData(
    @SerializedName("id") val id: String,
    @SerializedName("degree") val degree: String,
    @SerializedName("institution") val institution: String,
    @SerializedName("graduationDate") val graduationDate: String
)