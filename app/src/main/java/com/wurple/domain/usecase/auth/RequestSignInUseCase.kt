package com.wurple.domain.usecase.auth

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.AuthGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.InputValidator
import kotlin.coroutines.CoroutineContext

class RequestSignInUseCase(
    private val authGateway: AuthGateway,
    private val emailInputValidationRule: InputValidationRule<String>,
    private val inputValidator: InputValidator,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, RequestSignInUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        inputValidator.validate(emailInputValidationRule.validate(params.email))
        authGateway.requestSignIn(params.email)
    }

    class Params(val email: String)
}