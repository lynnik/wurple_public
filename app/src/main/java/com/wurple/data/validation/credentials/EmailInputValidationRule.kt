package com.wurple.data.validation.credentials

import android.content.Context
import android.util.Patterns
import com.wurple.R
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResultErrorMessage

class EmailInputValidationRule(
    private val context: Context
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val email = value?.trim().orEmpty()
        when {
            email.isEmpty() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Email(
                        context.getString(R.string.input_validation_error_empty_email)
                    )
                )
            }
            Patterns.EMAIL_ADDRESS.matcher(email).matches().not() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Email(
                        context.getString(R.string.input_validation_error_incorrect_email)
                    )
                )
            }
        }
        return ValidationResult.Success
    }
}