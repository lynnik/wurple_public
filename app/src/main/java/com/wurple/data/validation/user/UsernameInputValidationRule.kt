package com.wurple.data.validation.user

import android.content.Context
import com.wurple.R
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.domain.validation.size.MaxLength
import com.wurple.domain.validation.size.MinLength

class UsernameInputValidationRule(
    private val context: Context
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val username = value?.trim().orEmpty()
        when {
            username.isEmpty() -> {
                // this will help us to delete the username
                return ValidationResult.Success
            }
            (username.length in MinLength.USER_USERNAME..MaxLength.USER_USERNAME).not() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Username(
                        context.getString(
                            R.string.input_validation_error_incorrect_username_length,
                            MinLength.USER_USERNAME,
                            MaxLength.USER_USERNAME
                        )
                    )
                )
            }
            username.matches(Regex(USERNAME_PATTERN)).not() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Username(
                        context.getString(R.string.input_validation_error_incorrect_username)
                    )
                )
            }
        }
        return ValidationResult.Success
    }

    companion object {
        private const val USERNAME_PATTERN = "^[a-z0-9_.]+\$"
    }
}