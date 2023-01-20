package com.wurple.presentation.flow.base.activity

import androidx.appcompat.app.AppCompatActivity
import com.wurple.R

abstract class BaseActivity : AppCompatActivity() {
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