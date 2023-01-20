package com.wurple.presentation.flow.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.NavigationEvent
import com.wurple.domain.navigation.NavigationManager
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.base.CompletionBlock
import com.wurple.domain.usecase.base.CoroutineUseCase
import com.wurple.domain.usecase.base.FlowBlock
import com.wurple.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val logger: Logger
) : ViewModel(), NavigationManager {
    val isProgressVisibleLiveData = MutableLiveData(false)
    val errorMessageLiveData = SingleLiveEvent<String>()

    private val navigationEventSingleLiveEvent: SingleLiveEvent<NavigationEvent> = SingleLiveEvent()
    val navigationEventLiveData: LiveData<NavigationEvent> = navigationEventSingleLiveEvent

    override fun navigateForwardTo(screen: Screen) {
        navigationEventSingleLiveEvent.value = NavigationEvent.ForwardTo(screen)
    }

    override fun navigateForwardToAndCloseCurrent(screen: Screen) {
        navigationEventSingleLiveEvent.value = NavigationEvent.ForwardToAndCloseCurrent(screen)
    }

    override fun navigateForwardToAsRoot(screen: Screen) {
        navigationEventSingleLiveEvent.value = NavigationEvent.ForwardToAsRoot(screen)
    }

    override fun navigateBackward() {
        navigationEventSingleLiveEvent.value = NavigationEvent.Backward
    }

    override fun navigateBackwardTo(screen: Screen) {
        navigationEventSingleLiveEvent.value = NavigationEvent.BackwardTo(screen)
    }

    protected open fun showProgress() {
        isProgressVisibleLiveData.value = true
    }

    protected open fun hideProgress() {
        isProgressVisibleLiveData.value = false
    }

    protected fun isProgressVisible(): Boolean = isProgressVisibleLiveData.value ?: false

    protected fun showError(error: String?) {
        errorMessageLiveData.value = error.orEmpty()
    }

    protected open fun handleError(throwable: Throwable) {
        logger.e(this.javaClass.simpleName, throwable)
        hideProgress()
    }

    protected fun <T, R, U : CoroutineUseCase<T, R>> U.launch(param: R, block: CompletionBlock<T>) {
        viewModelScope.launch { execute(param, block) }
    }

    protected fun <T, R, U : FlowUseCase<T, R>> U.launch(param: R, block: FlowBlock<T>) {
        viewModelScope.launch { execute(param, block) }
    }
}
