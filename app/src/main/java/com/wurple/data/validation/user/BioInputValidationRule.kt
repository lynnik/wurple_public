package com.wurple.data.validation.user

import android.content.Context
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResult

class BioInputValidationRule(
    private val context: Context
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val bio = value?.trim().orEmpty()
        return ValidationResult.Success
    }
}