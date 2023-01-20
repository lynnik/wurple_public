package com.wurple.presentation.flow.more.support

import com.wurple.data.Wurple
import com.wurple.domain.log.Logger
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class SupportViewModel(
    private val wurple: Wurple,
    logger: Logger
) : BaseViewModel(logger) {
    val emailLiveData = SingleLiveEvent<String>()
    val copyEmailLiveData = SingleLiveEvent<String>()
    val telegramChannelLiveData = SingleLiveEvent<String>()

    fun navigateBack() {
        navigateBackward()
    }

    fun navigateEmail() {
        emailLiveData.value = wurple.supportEmail
    }

    fun copyEmail() {
        copyEmailLiveData.value = wurple.supportEmail
    }

    fun navigateTelegram() {
        telegramChannelLiveData.value = wurple.supportTelegram
    }
}