package com.wurple.presentation.flow.previews.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wurple.R

class PreviewsItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val mediumMargin = parent.resources.getDimensionPixelSize(R.dimen.size_medium)
        val position = parent.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION) {
            when (position) {
                0 -> outRect.set(0, mediumMargin, 0, mediumMargin)
                else -> outRect.set(0, 0, 0, mediumMargin)
            }
        }
    }
}