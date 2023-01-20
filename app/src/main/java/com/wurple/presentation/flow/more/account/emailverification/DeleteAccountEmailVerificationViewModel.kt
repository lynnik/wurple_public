package com.wurple.presentation.flow.more.account.emailverification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.usecase.auth.RequestDeleteAccountUseCase
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DeleteAccountEmailVerificationViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val requestDeleteAccountUseCase: RequestDeleteAccountUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val userLiveData = MutableLiveData<User>()
    val secondsLiveData = MutableLiveData<Int>()
    val resendLiveData = MutableLiveData<Boolean>()

    private var secondsJob: Job? = null
    private var seconds: Int = DURATION - 1

    init {
        observeUser()
        startTimer()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun requestSignIn() {
        requestDeleteAccountUseCase.launch(
            param = RequestDeleteAccountUseCase.Params(),
            block = {
                onComplete = {
                    resendLiveData.value = false
                    startTimer()
                }
                onError = ::handleError
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user -> userLiveData.value = user }
            }
        )
    }

    private fun startTimer() {
        setSeconds(DURATION - 1)
        secondsJob?.cancel()
        secondsJob = flow {
            while (seconds > 0) {
                delay(DELAY)
                emit(seconds - 1)
            }
        }
            .onEach {
                if (it == 0) {
                    resendLiveData.value = true
                }
                setSeconds(it)
            }
            .launchIn(viewModelScope)
    }

    private fun setSeconds(newSeconds: Int) {
        seconds = newSeconds
        secondsLiveData.value = seconds
    }

    companion object {
        const val DURATION = 60
        private const val DELAY = 1000L
    }
}