package com.wurple.presentation.flow.base.fragment

import androidx.annotation.LayoutRes
import com.wurple.domain.navigation.NavigationManager
import com.wurple.domain.navigation.Screen
import com.wurple.presentation.flow.base.activity.BaseNavigationActivity

abstract class BaseNavigationFragment(
    @LayoutRes layoutResId: Int
) : BaseFullscreenFragment(layoutResId), NavigationManager {
    override fun navigateForwardTo(screen: Screen) {
        (activity as? BaseNavigationActivity)?.navigateForwardTo(screen)
    }

    override fun navigateForwardToAndCloseCurrent(screen: Screen) {
        (activity as? BaseNavigationActivity)?.navigateForwardToAndCloseCurrent(screen)
    }

    override fun navigateForwardToAsRoot(screen: Screen) {
        (activity as? BaseNavigationActivity)?.navigateForwardToAsRoot(screen)
    }

    override fun navigateBackward() {
        (activity as? BaseNavigationActivity)?.navigateBackward()
    }

    override fun navigateBackwardTo(screen: Screen) {
        (activity as? BaseNavigationActivity)?.navigateBackwardTo(screen)
    }
}