package com.wurple.domain.validation

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: ValidationResultErrorMessage) : ValidationResult()
}