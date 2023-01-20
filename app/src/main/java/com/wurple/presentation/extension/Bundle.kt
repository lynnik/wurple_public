package com.wurple.presentation.extension

import android.os.Bundle

inline fun <reified T> Bundle?.extract(key: String, defaultValue: T): T {
    val result = this?.get(key) ?: defaultValue
    if (result != null && result !is T) {
        throw ClassCastException("Property $key has different class type")
    }
    return result as T
}