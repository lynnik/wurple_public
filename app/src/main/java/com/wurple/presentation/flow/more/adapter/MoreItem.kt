package com.wurple.presentation.flow.more.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class MoreItem(
    @DrawableRes val iconResId: Int,
    @StringRes val titleResId: Int
) {
    data class Account(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : MoreItem(iconResIdValue, titleResIdValue)

    data class Support(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : MoreItem(iconResIdValue, titleResIdValue)

    data class InAppPurchases(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : MoreItem(iconResIdValue, titleResIdValue)

    data class InviteFriend(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : MoreItem(iconResIdValue, titleResIdValue)

    data class Settings(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : MoreItem(iconResIdValue, titleResIdValue)

    data class AboutApp(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : MoreItem(iconResIdValue, titleResIdValue)
}