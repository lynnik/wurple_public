package com.wurple.domain.navigation

sealed class Screen(val tag: String) {
    data class SignIn(val deepLink: String? = null) : Screen("SignIn")
    data class SignInEmailVerification(val email: String) : Screen("SignInEmailVerification")
    data class Main(val deepLink: String? = null) : Screen("Main")
    object Onboarding : Screen("Onboarding")

    object EditProfile : Screen("EditProfile")
    object EditProfileImage : Screen("EditProfileImage")
    object EditProfileName : Screen("EditProfileName")
    object EditProfileDob : Screen("EditProfileDob")
    object EditProfileLocation : Screen("EditProfileLocation")
    object EditProfileEmail : Screen("EditProfileEmail")
    object CreateEditProfileEmailRequest : Screen("CreateEditProfileEmailRequest")
    object EditProfileEmailEmailVerification : Screen("EditProfileEmailEmailVerification")
    object EditProfileUsername : Screen("EditProfileUsername")
    object EditProfileBio : Screen("EditProfileBio")
    object EditProfileSkills : Screen("EditProfileSkills")
    data class EditProfileSkillsAdd(
        val skillId: String? = null
    ) : Screen("EditProfileSkillsAdd")
    object EditProfileExperience : Screen("EditProfileExperience")
    data class EditProfileExperienceAdd(
        val experienceId: String? = null
    ) : Screen("EditProfileExperienceAdd")
    object EditProfileEducation : Screen("EditProfileEducation")
    data class EditProfileEducationAdd(
        val educationId: String? = null
    ) : Screen("EditProfileEducationAdd")
    object EditProfileLanguages : Screen("EditProfileLanguages")
    data class EditProfileLanguagesAdd(
        val languageId: Int? = null,
        val languageLevelId: Int? = null
    ) : Screen("EditProfileLanguagesAdd")

    object Account : Screen("Account")
    object CreateDeleteAccountRequest : Screen("CreateDeleteAccountRequest")
    object DeleteAccountEmailVerification : Screen("DeleteAccountEmailVerification")
    object DeleteAccount : Screen("DeleteAccount")
    object Support : Screen("Support")
    object InviteFriend : Screen("InviteFriend")
    object AboutApp : Screen("AboutApp")

    data class PreviewsAdd(val previewId: String? = null) : Screen("PreviewsAdd")
    object TempPreview : Screen("TempPreview")
    data class Preview(val previewId: String) : Screen("Preview")
}
