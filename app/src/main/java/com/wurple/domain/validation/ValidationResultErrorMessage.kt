package com.wurple.domain.validation

sealed class ValidationResultErrorMessage {
    data class Email(val message: String) : ValidationResultErrorMessage()
    data class FirstName(val message: String) : ValidationResultErrorMessage()
    data class LastName(val message: String) : ValidationResultErrorMessage()
    data class Dob(val message: String) : ValidationResultErrorMessage()
    data class Username(val message: String) : ValidationResultErrorMessage()
    data class Bio(val message: String) : ValidationResultErrorMessage()
    data class Skill(val message: String) : ValidationResultErrorMessage()
    data class Position(val message: String) : ValidationResultErrorMessage()
    data class CompanyName(val message: String) : ValidationResultErrorMessage()
    data class PositionDateFrom(val message: String) : ValidationResultErrorMessage()
    data class PositionDateTo(val message: String) : ValidationResultErrorMessage()
    data class Degree(val message: String) : ValidationResultErrorMessage()
    data class Institution(val message: String) : ValidationResultErrorMessage()
    data class GraduationDate(val message: String) : ValidationResultErrorMessage()
    data class PreviewTitle(val message: String) : ValidationResultErrorMessage()
}