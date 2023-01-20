package com.wurple.data.validation.user

import android.content.Context
import com.wurple.R
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResultErrorMessage

class FirstNameInputValidationRule(
    private val context: Context
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val firstName = value?.trim().orEmpty()
        when {
            firstName.isEmpty() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.FirstName(
                        context.getString(R.string.input_validation_error_empty_first_name)
                    )
                )
            }
        }
        return ValidationResult.Success
    }
}