package com.wurple.domain.log

interface Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, throwable: Throwable)
}