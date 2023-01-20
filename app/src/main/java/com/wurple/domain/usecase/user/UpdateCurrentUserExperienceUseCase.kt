package com.wurple.domain.usecase.user

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.model.user.UserExperienceUpdateRequest
import com.wurple.domain.usecase.base.CoroutineUseCase
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.InputValidator
import com.wurple.domain.validation.ValidationResult
import kotlin.coroutines.CoroutineContext

class UpdateCurrentUserExperienceUseCase(
    private val positionInputValidationRule: InputValidationRule<String>,
    private val companyNameInputValidationRule: InputValidationRule<String>,
    private val positionDateFromInputValidationRule: InputValidationRule<String>,
    private val positionDateToInputValidationRule: InputValidationRule<String>,
    private val inputValidator: InputValidator,
    private val userGateway: UserGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, UpdateCurrentUserExperienceUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        if (params.userExperienceUpdateRequest.isDelete.not()) {
            val validationResults = mutableListOf<ValidationResult>()
            validationResults.add(
                positionInputValidationRule.validate(params.userExperienceUpdateRequest.position)
            )
            validationResults.add(
                companyNameInputValidationRule.validate(params.userExperienceUpdateRequest.companyName)
            )
            validationResults.add(
                positionDateFromInputValidationRule.validate(params.userExperienceUpdateRequest.fromDate)
            )
            if (params.userExperienceUpdateRequest.isCurrent.not()) {
                validationResults.add(
                    positionDateToInputValidationRule.validate(params.userExperienceUpdateRequest.toDate)
                )
            }
            if (validationResults.isNotEmpty()) {
                inputValidator.validate(*validationResults.toTypedArray())
            }
        }
        userGateway.updateCurrentUserExperience(params.userExperienceUpdateRequest)
    }

    class Params(val userExperienceUpdateRequest: UserExperienceUpdateRequest)
}