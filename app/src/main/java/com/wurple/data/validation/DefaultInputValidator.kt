package com.wurple.data.validation

import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.InputValidator
import com.wurple.domain.validation.ValidationResult

class DefaultInputValidator : InputValidator {
    override fun validate(vararg validationResults: ValidationResult) {
        val validationErrors = validationResults.mapNotNull { it as? ValidationResult.Error }
        if (validationErrors.isNotEmpty()) {
            throw InputValidationException(validationErrors)
        }
    }
}