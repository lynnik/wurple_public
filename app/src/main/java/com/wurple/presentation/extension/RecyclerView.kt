package com.wurple.presentation.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setOnScrolledToBottomListener(onScrolledToBottom: () -> Unit) {
    addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = layoutManager as? LinearLayoutManager
                    ?: throw IllegalStateException("It works only for LinearLayoutManager")
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                    firstVisibleItemPosition >= 0
                ) {
                    onScrolledToBottom()
                }
            }
        }
    )
}

fun RecyclerView.isScrolled(): Boolean {
    return computeVerticalScrollOffset() > 0
}