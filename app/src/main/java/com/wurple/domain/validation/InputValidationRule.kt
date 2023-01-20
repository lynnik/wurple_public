package com.wurple.domain.validation

interface InputValidationRule<Value> {
    fun validate(value: Value?): ValidationResult
}