package com.wurple.domain.model.common

sealed class StatusValue(val value: Int) {
    object Enabled : StatusValue(0)
    object Disabled : StatusValue(1)
    object Deleted : StatusValue(2)

    companion object {
        fun getByValue(value: Int): StatusValue {
            return when (value) {
                Enabled.value -> Enabled
                Disabled.value -> Disabled
                Deleted.value -> Deleted
                else -> throw IllegalArgumentException(
                    "There is no StatusValue with value: $value"
                )
            }
        }
    }
}