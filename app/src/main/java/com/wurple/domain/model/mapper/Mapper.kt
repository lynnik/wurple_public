package com.wurple.domain.model.mapper

interface Mapper<From, To> {
    suspend fun map(from: From): To
}