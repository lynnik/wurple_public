package com.wurple.presentation.extension

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import com.wurple.BuildConfig
import com.wurple.R

inline val Context.clipboardManager: ClipboardManager?
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.getColorCompat(@ColorRes colorResId: Int): Int =
    ContextCompat.getColor(this, colorResId)

fun Context.getColorCompatByAttr(@AttrRes attrResId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrResId, typedValue, true)
    return getColorCompat(typedValue.resourceId)
}

fun Context.getDrawableCompat(@DrawableRes drawableResId: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableResId)

fun Context.getDrawableCompatByAttr(@AttrRes attrResId: Int): Drawable? {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrResId, typedValue, true)
    return getDrawableCompat(typedValue.resourceId)
}

fun Context.openAppSettings() {
    startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
        )
    )
}

fun Context.composeEmail(addresses: Array<String>, subject: String? = null, text: String? = null) {
    val intent = Intent(Intent.ACTION_SENDTO)
        .setData(Uri.parse("mailto:"))
        .putExtra(Intent.EXTRA_EMAIL, addresses)
        .putExtra(Intent.EXTRA_SUBJECT, subject)
        .putExtra(Intent.EXTRA_TEXT, text)
    if (intent.resolveActivity(packageManager) == null) {
        toast(getString(R.string.error_there_is_no_email_app))
    } else {
        startActivity(intent)
    }
}

fun Context.openWebPage(container: View, url: String) {
    try {
        val colorSurface =
            MaterialColors.getColor(this, R.attr.colorSurface, "Wurple surface color attr")
        val customTabColorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorSurface)
            .setSecondaryToolbarColor(colorSurface)
            .setNavigationBarColor(colorSurface)
            .setNavigationBarDividerColor(colorSurface)
            .build()
        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(customTabColorSchemeParams)
            .build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    } catch (throwable: Throwable) {
        container.snackbar(getString(R.string.error_there_is_no_website_app))
    }
}

fun Context.share(text: String) {
    val intent = Intent(Intent.ACTION_SEND)
        .setType("text/plain")
        .putExtra(Intent.EXTRA_TEXT, text)
    if (intent.resolveActivity(packageManager) == null) {
        toast(getString(R.string.error_there_is_no_share_text_app))
    } else {
        startActivity(Intent.createChooser(intent, null))
    }
}

fun Context.openPlayMarket(container: View, packageName: String) {
    val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    if (marketIntent.resolveActivity(packageManager) == null) {
        openWebPage(
            container,
            "https://play.google.com/store/apps/details?id=$packageName"
        )
    } else {
        startActivity(marketIntent)
    }
}

fun Context.openTelegramChannel(container: View, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(packageManager) == null) {
        container.snackbar(getString(R.string.error_there_is_no_telegram_app))
    } else {
        startActivity(intent)
    }
}

fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (throwable: Throwable) {
        false
    }
}

fun Context.highlightText(fullText: CharSequence, partToHighlight: String): CharSequence {
    val coloredTextStart = fullText.indexOf(partToHighlight)
    val coloredTextEnd = coloredTextStart + partToHighlight.length
    val spannableString = SpannableString(fullText)
    spannableString.setSpan(
        ForegroundColorSpan(getColorCompatByAttr(R.attr.colorTertiary)),
        coloredTextStart,
        coloredTextEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}
