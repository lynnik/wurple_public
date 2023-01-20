package com.wurple.presentation.flow.base.activity

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.wurple.domain.navigation.NavigationManager

abstract class BaseFullscreenActivity : BaseActivity(), NavigationManager {
    fun setTopSystemBarInset(v: View) {
        ViewCompat.setOnApplyWindowInsetsListener(v) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }
    }

    fun setBottomSystemBarInset(v: View) {
        ViewCompat.setOnApplyWindowInsetsListener(v) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    protected fun makeFullscreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}