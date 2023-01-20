package com.wurple.data.model.user

import com.google.gson.annotations.SerializedName

data class UserExperienceData(
    @SerializedName("id") val id: String,
    @SerializedName("position") val position: String,
    @SerializedName("companyName") val companyName: String,
    @SerializedName("isCurrent") val isCurrent: Boolean,
    @SerializedName("fromDate") val fromDate: String,
    @SerializedName("toDate") val toDate: String
)