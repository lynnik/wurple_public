package com.wurple.presentation.extension

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

fun TextView.clickableText(
    clickablePart: String,
    onClick: () -> Unit
) {
    val fullText = this.text
    val clickableTextStart = fullText.indexOf(clickablePart)
    val clickableTextEnd = clickableTextStart + clickablePart.length
    val spannableString = SpannableString(fullText)
    spannableString.setSpan(
        object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        },
        clickableTextStart,
        clickableTextEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.movementMethod = LinkMovementMethod.getInstance()
    this.text = spannableString
}