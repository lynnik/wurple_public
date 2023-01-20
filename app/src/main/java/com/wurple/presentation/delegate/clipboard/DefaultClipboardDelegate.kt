package com.wurple.presentation.delegate.clipboard

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import com.wurple.R
import com.wurple.presentation.extension.clipboardManager
import com.wurple.presentation.extension.snackbar

class DefaultClipboardDelegate : ClipboardDelegate {
    private var context: Context? = null

    override fun attachClipboardDelegate(context: Context?) {
        this.context = context
    }

    override fun copyToClipboard(container: View, text: String) {
        context?.let {
            val label = it.getString(R.string.common_copied)
            val clipData = ClipData.newPlainText(label, text)
            it.clipboardManager?.setPrimaryClip(clipData)
            container.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                container.snackbar(label)
            }
        }
    }
}
