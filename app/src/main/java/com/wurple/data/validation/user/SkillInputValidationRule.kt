package com.wurple.data.validation.user

import android.content.Context
import com.wurple.R
import com.wurple.domain.validation.ValidationResult
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.domain.validation.size.MaxLength

class SkillInputValidationRule(
    private val context: Context
) : InputValidationRule<String> {
    override fun validate(value: String?): ValidationResult {
        val skill = value?.trim().orEmpty()
        when {
            skill.isEmpty() -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Skill(
                        context.getString(R.string.input_validation_error_empty_skill)
                    )
                )
            }
            skill.length > MaxLength.USER_SKILL -> {
                return ValidationResult.Error(
                    ValidationResultErrorMessage.Skill(
                        context.getString(
                            R.string.input_validation_error_max_length_skill,
                            MaxLength.USER_SKILL,
                            skill
                        )
                    )
                )
            }
        }
        return ValidationResult.Success
    }
}