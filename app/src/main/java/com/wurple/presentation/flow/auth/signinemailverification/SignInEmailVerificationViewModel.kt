package com.wurple.presentation.flow.auth.signinemailverification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.auth.RequestSignInUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SignInEmailVerificationViewModel(
    val email: String,
    private val requestSignInUseCase: RequestSignInUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val secondsLiveData = MutableLiveData<Int>()
    val resendLiveData = MutableLiveData<Boolean>()

    private var secondsJob: Job? = null
    private var seconds: Int = DURATION - 1

    init {
        startTimer()
    }

    fun navigateSupport() {
        navigateForwardTo(Screen.Support)
    }

    fun requestSignIn() {
        requestSignInUseCase.launch(
            param = RequestSignInUseCase.Params(email),
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

    fun onChangeEmail() {
        navigateBackward()
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