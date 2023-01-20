package com.wurple.presentation.flow.more.support.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class SupportItem(
    @DrawableRes val iconResId: Int,
    @StringRes val titleResId: Int
) {
    data class Email(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : SupportItem(iconResIdValue, titleResIdValue)

    data class Telegram(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : SupportItem(iconResIdValue, titleResIdValue)
}