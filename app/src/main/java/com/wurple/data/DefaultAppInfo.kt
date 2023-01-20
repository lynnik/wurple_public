package com.wurple.data

import com.wurple.BuildConfig
import com.wurple.domain.AppInfo

class DefaultAppInfo : AppInfo {
    override val packageName: String
        get() = BuildConfig.APPLICATION_ID

    override val version: String
        get() = BuildConfig.VERSION_NAME
}