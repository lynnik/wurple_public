package com.wurple.presentation.flow.more.account.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class AccountItem(
    @DrawableRes val iconResId: Int,
    @StringRes val titleResId: Int
) {
    data class EditEmail(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : AccountItem(iconResIdValue, titleResIdValue)

    data class SignOut(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : AccountItem(iconResIdValue, titleResIdValue)

    data class DeleteAccount(
        @DrawableRes val iconResIdValue: Int,
        @StringRes val titleResIdValue: Int
    ) : AccountItem(iconResIdValue, titleResIdValue)
}
