package com.wurple.presentation.extension

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

fun Activity.toast(text: String) {
    (this as Context).toast(text)
}

fun Activity.getColorCompat(@ColorRes colorResId: Int): Int =
    (this as Context).getColorCompat(colorResId)

fun Activity.getColorCompatByAttr(@AttrRes attrResId: Int): Int =
    (this as Context).getColorCompatByAttr(attrResId)

fun Activity.getDrawableCompat(@DrawableRes drawableResId: Int): Drawable? =
    (this as Context).getDrawableCompat(drawableResId)

fun Activity.getDrawableCompatByAttr(@AttrRes attrResId: Int): Drawable? =
    (this as Context).getDrawableCompatByAttr(attrResId)

fun Activity.openAppSettings() {
    (this as Context).openAppSettings()
}