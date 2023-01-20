package com.wurple.domain.usecase.user

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.model.user.UserUpdateRequest
import com.wurple.domain.usecase.base.CoroutineUseCase
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.InputValidator
import com.wurple.domain.validation.ValidationResult
import kotlin.coroutines.CoroutineContext

class UpdateCurrentUserUseCase(
    private val firstNameInputValidationRule: InputValidationRule<String>,
    private val lastNameInputValidationRule: InputValidationRule<String>,
    private val dobInputValidationRule: InputValidationRule<String>,
    private val usernameInputValidationRule: InputValidationRule<String>,
    private val bioInputValidationRule: InputValidationRule<String>,
    private val inputValidator: InputValidator,
    private val userGateway: UserGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, UpdateCurrentUserUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        val validationResults = mutableListOf<ValidationResult>()
        params.userUpdateRequest.firstName?.let {
            validationResults.add(firstNameInputValidationRule.validate(it))
        }
        params.userUpdateRequest.lastName?.let {
            validationResults.add(lastNameInputValidationRule.validate(it))
        }
        params.userUpdateRequest.dob?.let {
            validationResults.add(dobInputValidationRule.validate(it))
        }
        params.userUpdateRequest.username?.let {
            validationResults.add(usernameInputValidationRule.validate(it))
        }
        params.userUpdateRequest.bio?.let {
            validationResults.add(bioInputValidationRule.validate(it))
        }
        if (validationResults.isNotEmpty()) {
            inputValidator.validate(*validationResults.toTypedArray())
        }
        userGateway.updateCurrentUser(params.userUpdateRequest)
    }

    class Params(val userUpdateRequest: UserUpdateRequest)
}