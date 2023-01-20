package com.wurple.presentation.flow.more

import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class MoreViewModel(
    logger: Logger
) : BaseViewModel(logger) {
    fun navigateAccount() {
        navigateForwardTo(Screen.Account)
    }

    fun navigateSupport() {
        navigateForwardTo(Screen.Support)
    }

    fun navigateInviteFriend() {
        navigateForwardTo(Screen.InviteFriend)
    }

    fun navigateAboutApp() {
        navigateForwardTo(Screen.AboutApp)
    }
}