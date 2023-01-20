package com.wurple.domain.error.handler

interface ErrorHandler {
    fun handleError(error: Throwable): Throwable
}