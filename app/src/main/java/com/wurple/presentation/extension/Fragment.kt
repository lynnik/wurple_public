package com.wurple.presentation.extension

import android.graphics.drawable.Drawable
import androidx.activity.OnBackPressedCallback
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

fun Fragment.toast(text: String) {
    requireContext().toast(text)
}

fun Fragment.getColorCompat(@ColorRes colorResId: Int): Int =
    requireContext().getColorCompat(colorResId)

fun Fragment.getColorCompatByAttr(@AttrRes attrResId: Int): Int =
    requireContext().getColorCompatByAttr(attrResId)

fun Fragment.getDrawableCompat(@DrawableRes drawableResId: Int): Drawable? =
    requireContext().getDrawableCompat(drawableResId)

fun Fragment.getDrawableCompatByAttr(@AttrRes attrResId: Int): Drawable? =
    requireContext().getDrawableCompatByAttr(attrResId)

fun Fragment.openAppSettings() {
    requireContext().openAppSettings()
}

fun Fragment.onBackPressed(action: () -> Unit) {
    activity?.onBackPressedDispatcher?.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                action()
            }
        }
    )
}

fun <T : Fragment> T.withArguments(vararg params: Pair<String, Any?>): T {
    arguments = bundleOf(*params)
    return this
}

fun Fragment.setScreenBrightness(brightness: Float) {
    val windowLayoutParams = activity?.window?.attributes
    windowLayoutParams?.screenBrightness = brightness
    activity?.window?.attributes = windowLayoutParams
}
