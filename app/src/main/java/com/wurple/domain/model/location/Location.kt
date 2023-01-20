package com.wurple.domain.model.location

data class Location(
    val id: String,
    val country: String,
    val city: String,
    val formattedLocation: String
)