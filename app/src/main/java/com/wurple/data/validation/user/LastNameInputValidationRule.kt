package com.wurple.data.validation.user

import android.content.Context
import com.wurple.R
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResultErrorMessage

class LastNameInputValidationRule(
    private val context: Context
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val lastName = value?.trim().orEmpty()
        when {
            lastName.isEmpty() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.LastName(
                        context.getString(R.string.input_validation_error_empty_last_name)
                    )
                )
            }
        }
        return ValidationResult.Success
    }
}