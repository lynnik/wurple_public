package com.wurple.data.error

import com.wurple.domain.error.handler.ErrorHandler

class DataErrorHandler : ErrorHandler {
    override fun handleError(error: Throwable): Throwable = error
}