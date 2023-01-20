package com.wurple.data.model.preview

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preview")
data class PreviewStorageData(
    @PrimaryKey val id: String,
    val title: String,
    val expDate: String,
    val isExpired: Boolean,
    val previewLifetimeOption: Int,

    val userId: String,
    // visibility values
    val userImageVisibility: Int,
    val userNameVisibility: Int,
    val userCurrentPositionVisibility: Int,
    val userLocationVisibility: Int,
    val userEmailVisibility: Int,
    val userUsernameVisibility: Int,
    val userBioVisibility: Int,
    val userSkillsVisibility: Int,
    val userExperienceVisibility: Int,
    val userEducationVisibility: Int,
    val userLanguagesVisibility: Int,

    // properties
    val statusValue: Int,
    val statusDescription: String,
    val createdAt: String,
    val updatedAt: String
)