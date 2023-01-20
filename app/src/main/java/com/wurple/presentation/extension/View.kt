package com.wurple.presentation.extension

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DimenRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.Snackbar

fun View.visible(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.invisible(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun View.visibleOrGone(isVisible: Boolean): View {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
    return this
}

fun View.snackbar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

fun View.hideKeyboard(): Boolean =
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (error: Throwable) {
        false
    }

fun View.margins(
    @DimenRes left: Int? = null,
    @DimenRes top: Int? = null,
    @DimenRes right: Int? = null,
    @DimenRes bottom: Int? = null
) {
    val tempLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    tempLayoutParams.setMargins(
        left?.let { resources.getDimensionPixelSize(it) } ?: tempLayoutParams.leftMargin,
        top?.let { resources.getDimensionPixelSize(it) } ?: tempLayoutParams.topMargin,
        right?.let { resources.getDimensionPixelSize(it) } ?: tempLayoutParams.rightMargin,
        bottom?.let { resources.getDimensionPixelSize(it) } ?: tempLayoutParams.bottomMargin
    )
    layoutParams = tempLayoutParams
}

fun View.applyViewNavigationBarsInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bottomInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        view.updatePadding(bottom = bottomInsets)
        insets
    }
}

fun View.applyViewStatusBarsInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val topInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
        view.updatePadding(top = topInsets)
        insets
    }
}