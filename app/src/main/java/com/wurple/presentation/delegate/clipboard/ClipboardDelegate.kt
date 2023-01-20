package com.wurple.presentation.delegate.clipboard

import android.content.Context
import android.view.View

interface ClipboardDelegate {
    fun attachClipboardDelegate(context: Context?)
    fun copyToClipboard(container: View, text: String)
}
