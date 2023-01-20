package com.wurple.data.validation.user

import android.content.Context
import com.wurple.R
import com.wurple.domain.date.DateManager
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.ValidationResultErrorMessage

class GraduationDateInputValidationRule(
    private val context: Context,
    private val dateManager: DateManager
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val dob = value?.trim().orEmpty()
        when {
            dob.isEmpty() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.GraduationDate(
                        context.getString(R.string.input_validation_error_empty_default_date)
                    )
                )
            }
            dateManager.isDateValid(dob).not() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.GraduationDate(
                        context.getString(R.string.input_validation_error_incorrect_default_date)
                    )
                )
            }
        }
        return ValidationResult.Success
    }
}