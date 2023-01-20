package com.wurple.presentation.flow.base.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wurple.R

abstract class BaseBottomSheetDialogFragment(
    @LayoutRes private val layoutResId: Int
) : BottomSheetDialogFragment() {
    abstract val binding: ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId, container, false)
    }

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