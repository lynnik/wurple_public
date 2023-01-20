package com.wurple.domain.validation

class InputValidationException(
    val errors: List<ValidationResult.Error>
) : Throwable()