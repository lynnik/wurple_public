package com.wurple.data.validation.preview

import android.content.Context
import com.wurple.R
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResultErrorMessage

class PreviewTitleInputValidationRule(
    private val context: Context
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val title = value?.trim().orEmpty()
        when {
            title.isEmpty() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.PreviewTitle(
                        context.getString(R.string.input_validation_error_empty_preview_title)
                    )
                )
            }
        }
        return ValidationResult.Success
    }
}