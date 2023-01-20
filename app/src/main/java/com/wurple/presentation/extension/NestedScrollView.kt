package com.wurple.presentation.extension

import androidx.core.widget.NestedScrollView

fun NestedScrollView.isScrolled(): Boolean {
    return scrollY > 0
}