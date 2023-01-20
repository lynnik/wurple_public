package com.wurple.domain.usecase.user

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.model.user.UserEducationUpdateRequest
import com.wurple.domain.usecase.base.CoroutineUseCase
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.InputValidator
import com.wurple.domain.validation.ValidationResult
import kotlin.coroutines.CoroutineContext

class UpdateCurrentUserEducationUseCase(
    private val degreeInputValidationRule: InputValidationRule<String>,
    private val institutionInputValidationRule: InputValidationRule<String>,
    private val graduationDateFromInputValidationRule: InputValidationRule<String>,
    private val inputValidator: InputValidator,
    private val userGateway: UserGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, UpdateCurrentUserEducationUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        if (params.userEducationUpdateRequest.isDelete.not()) {
            val validationResults = mutableListOf<ValidationResult>()
            validationResults.add(
                degreeInputValidationRule.validate(params.userEducationUpdateRequest.degree)
            )
            validationResults.add(
                institutionInputValidationRule.validate(params.userEducationUpdateRequest.institution)
            )
            validationResults.add(
                graduationDateFromInputValidationRule.validate(params.userEducationUpdateRequest.graduationDate)
            )
            if (validationResults.isNotEmpty()) {
                inputValidator.validate(*validationResults.toTypedArray())
            }
        }
        userGateway.updateCurrentUserEducation(params.userEducationUpdateRequest)
    }

    class Params(val userEducationUpdateRequest: UserEducationUpdateRequest)
}