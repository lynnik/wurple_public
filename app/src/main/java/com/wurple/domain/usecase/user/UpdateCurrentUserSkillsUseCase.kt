package com.wurple.domain.usecase.user

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.model.user.UserSkillsUpdateRequest
import com.wurple.domain.usecase.base.CoroutineUseCase
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.InputValidator
import kotlin.coroutines.CoroutineContext

class UpdateCurrentUserSkillsUseCase(
    private val skillInputValidationRule: InputValidationRule<String>,
    private val inputValidator: InputValidator,
    private val userGateway: UserGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, UpdateCurrentUserSkillsUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        if (params.userSkillsUpdateRequest.isDelete.not()) {
            params.userSkillsUpdateRequest.userSkills.forEach { userSkill ->
                inputValidator.validate(skillInputValidationRule.validate(userSkill.title))
            }
        }
        userGateway.updateCurrentUserSkills(params.userSkillsUpdateRequest)
    }

    class Params(val userSkillsUpdateRequest: UserSkillsUpdateRequest)
}