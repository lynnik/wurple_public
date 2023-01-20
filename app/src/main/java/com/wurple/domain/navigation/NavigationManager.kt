package com.wurple.domain.navigation

interface NavigationManager {
    fun navigateForwardTo(screen: Screen)
    fun navigateForwardToAndCloseCurrent(screen: Screen)
    fun navigateForwardToAsRoot(screen: Screen)
    fun navigateBackward()
    fun navigateBackwardTo(screen: Screen)
}