package com.wurple.data.validation.user

import android.content.Context
import com.wurple.R
import com.wurple.domain.date.DateManager
import com.wurple.domain.model.user.User
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.ValidationResultErrorMessage

class DobInputValidationRule(
    private val context: Context,
    private val dateManager: DateManager
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val dob = value?.trim().orEmpty()
        when {
            dob.isEmpty() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Dob(
                        context.getString(R.string.input_validation_error_empty_default_date)
                    )
                )
            }
            dateManager.isDateValid(dob).not() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Dob(
                        context.getString(R.string.input_validation_error_incorrect_default_date)
                    )
                )
            }
            dateManager.checkIsUserAgeValid(dob).not() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Dob(
                        context.getString(
                            R.string.input_validation_error_incorrect_age,
                            User.MIN_AGE,
                            User.MAX_AGE
                        )
                    )
                )
            }
        }
        return ValidationResult.Success
    }
}