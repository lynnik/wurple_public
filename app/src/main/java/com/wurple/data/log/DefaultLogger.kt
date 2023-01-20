package com.wurple.data.log

import com.wurple.domain.log.Logger
import timber.log.Timber

class DefaultLogger : Logger {
    init {
        Timber.plant(LoggerTree())
    }

    override fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun e(tag: String, throwable: Throwable) {
        Timber.tag(tag).e(throwable)
    }
}