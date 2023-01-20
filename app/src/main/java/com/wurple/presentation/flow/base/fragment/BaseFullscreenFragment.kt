package com.wurple.presentation.flow.base.fragment

import android.view.View
import androidx.annotation.LayoutRes
import com.wurple.presentation.flow.base.activity.BaseFullscreenActivity

abstract class BaseFullscreenFragment(
    @LayoutRes layoutResId: Int
) : BaseFragment(layoutResId) {
    protected fun setTopSystemBarInset(v: View) {
        (activity as? BaseFullscreenActivity)?.setTopSystemBarInset(v)
    }

    protected fun setBottomSystemBarInset(v: View) {
        (activity as? BaseFullscreenActivity)?.setBottomSystemBarInset(v)
    }
}