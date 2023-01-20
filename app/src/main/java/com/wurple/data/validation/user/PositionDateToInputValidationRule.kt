package com.wurple.data.validation.user

import android.content.Context
import com.wurple.R
import com.wurple.domain.date.DateManager
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.ValidationResultErrorMessage

class PositionDateToInputValidationRule(
    private val context: Context,
    private val dateManager: DateManager
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val date = value?.trim().orEmpty()
        when {
            date.isEmpty() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.PositionDateTo(
                        context.getString(R.string.input_validation_error_empty_default_date)
                    )
                )
            }
            dateManager.isDateValid(date).not() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.PositionDateTo(
                        context.getString(R.string.input_validation_error_incorrect_default_date)
                    )
                )
            }
            dateManager.isDateInTheFuture(date) -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.PositionDateTo(
                        context.getString(R.string.input_validation_error_date_in_the_future)
                    )
                )
            }
        }
        return ValidationResult.Success
    }
}