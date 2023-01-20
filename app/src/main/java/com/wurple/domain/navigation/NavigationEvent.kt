package com.wurple.domain.navigation

sealed class NavigationEvent {
    data class ForwardTo(val screen: Screen) : NavigationEvent()
    data class ForwardToAndCloseCurrent(val screen: Screen) : NavigationEvent()
    data class ForwardToAsRoot(val screen: Screen) : NavigationEvent()
    object Backward : NavigationEvent()
    data class BackwardTo(val screen: Screen) : NavigationEvent()
}