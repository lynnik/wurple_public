package com.wurple.data.validation.user

import android.content.Context
import com.wurple.R
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResultErrorMessage

class DegreeInputValidationRule(
    private val context: Context
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val firstName = value?.trim().orEmpty()
        when {
            firstName.isEmpty() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Degree(
                        context.getString(R.string.input_validation_error_empty_degree)
                    )
                )
            }
        }
        return ValidationResult.Success
    }
}