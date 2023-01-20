package com.wurple.presentation.flow.main

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.deeplink.DeepLinkResult
import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.deeplink.HandleDeepLinkUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class MainViewModel(
    private val deepLink: String,
    private val handleDeepLinkUseCase: HandleDeepLinkUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val toolbarStateLiveData = MutableLiveData<ToolbarState>(ToolbarState.You)

    init {
        handleDeepLink()
    }

    fun setState(toolbarState: ToolbarState) {
        toolbarStateLiveData.value = toolbarState
    }

    private fun handleDeepLink() {
        handleDeepLinkUseCase.launch(
            param = HandleDeepLinkUseCase.Params(deepLink),
            block = {
                onComplete = { deepLinkResult ->
                    when (deepLinkResult) {
                        is DeepLinkResult.DeleteAccount -> {
                            navigateForwardTo(Screen.DeleteAccount)
                        }
                        is DeepLinkResult.EditProfileEmail -> {
                            navigateForwardTo(Screen.EditProfileEmail)
                        }
                        is DeepLinkResult.Preview -> {
                            navigateForwardTo(Screen.Preview(deepLinkResult.previewId))
                        }
                        else -> Unit
                    }
                }
                onError = ::handleError
            }
        )
    }

    sealed class ToolbarState {
        object You : ToolbarState()
        object Previews : ToolbarState()
        object More : ToolbarState()
    }
}
