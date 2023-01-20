package com.wurple.presentation.flow.base.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.wurple.R

abstract class BaseFragment(
    @LayoutRes layoutResId: Int
) : Fragment(layoutResId) {
    abstract val binding: ViewBinding

    protected open fun onProgressVisible() {
        /*NOOP*/
    }

    protected open fun onProgressGone() {
        /*NOOP*/
    }

    protected fun showErrorMessage(message: String) {
        onShowErrorMessage(
            if (message.isNotEmpty()) {
                message
            } else {
                getString(R.string.error_unexpected)
            }
        )
    }

    protected open fun onShowErrorMessage(error: String) {
        /*NOOP*/
    }
}