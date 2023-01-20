package com.wurple.presentation.flow.base.activity

import com.wurple.R
import com.wurple.domain.navigation.NavigationEvent
import com.wurple.domain.navigation.NavigationManager
import com.wurple.domain.navigation.Screen
import com.wurple.presentation.navigation.getFragmentByScreen

abstract class BaseNavigationActivity : BaseFullscreenActivity(), NavigationManager {
    fun handleNavigationEvent(navigationEvent: NavigationEvent) {
        when (navigationEvent) {
            is NavigationEvent.ForwardTo -> navigateForwardTo(navigationEvent.screen)
            is NavigationEvent.ForwardToAndCloseCurrent ->
                navigateForwardToAndCloseCurrent(navigationEvent.screen)
            is NavigationEvent.ForwardToAsRoot -> navigateForwardToAsRoot(navigationEvent.screen)
            NavigationEvent.Backward -> navigateBackward()
            is NavigationEvent.BackwardTo -> navigateBackwardTo(navigationEvent.screen)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            finish()
        }
    }

    override fun navigateForwardTo(screen: Screen) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.rootFragmentContainerView, getFragmentByScreen(screen), screen.tag)
            .addToBackStack(screen.tag)
            .commitAllowingStateLoss()
    }

    override fun navigateForwardToAndCloseCurrent(screen: Screen) {
        supportFragmentManager.popBackStack()
        navigateForwardTo(screen)
    }

    override fun navigateForwardToAsRoot(screen: Screen) {
        repeat(supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
        navigateForwardTo(screen)
    }

    override fun navigateBackward() {
        onBackPressed()
    }

    override fun navigateBackwardTo(screen: Screen) {
        supportFragmentManager.popBackStack(screen.tag, 0)
    }
}