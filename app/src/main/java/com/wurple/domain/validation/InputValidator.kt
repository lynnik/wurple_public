package com.wurple.domain.validation

interface InputValidator {
    fun validate(vararg validationResults: ValidationResult)
}