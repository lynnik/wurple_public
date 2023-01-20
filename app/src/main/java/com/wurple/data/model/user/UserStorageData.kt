package com.wurple.data.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserStorageData(
    @PrimaryKey val id: String,
    val referrerId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val imageUrl: String,
    val dob: String,
    val locationId: String,
    val country: String,
    val city: String,
    val username: String,
    val bio: String,
    // json object
    val skills: String,
    // json object
    val experience: String,
    // json object
    val education: String,
    // json object
    val languages: String,
    // properties
    val statusValue: Int,
    val statusDescription: String,
    val createdAt: String,
    val updatedAt: String
)