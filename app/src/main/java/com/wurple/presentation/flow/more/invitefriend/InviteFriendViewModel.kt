package com.wurple.presentation.flow.more.invitefriend

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.usecase.deeplink.GetInviteFriendDeepLinkUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class InviteFriendViewModel(
    private val getInviteFriendDeepLinkUseCase: GetInviteFriendDeepLinkUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val deepLinkLiveData = MutableLiveData<String>()

    init {
        getInviteFriendDeepLink()
    }

    fun navigateBack() {
        navigateBackward()
    }

    private fun getInviteFriendDeepLink() {
        getInviteFriendDeepLinkUseCase.launch(
            param = GetInviteFriendDeepLinkUseCase.Params(),
            block = {
                onComplete = { deepLink ->
                    deepLink?.let { deepLinkLiveData.value = it }
                }
                onError = {
                    handleError(it)
                    showError(it.message)
                }
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }
}