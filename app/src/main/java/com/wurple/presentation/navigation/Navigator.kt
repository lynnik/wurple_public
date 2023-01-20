package com.wurple.presentation.navigation

import com.wurple.domain.navigation.Screen
import com.wurple.presentation.flow.auth.signin.SignInFragment
import com.wurple.presentation.flow.auth.signinemailverification.SignInEmailVerificationFragment
import com.wurple.presentation.flow.base.fragment.BaseFragment
import com.wurple.presentation.flow.main.MainFragment
import com.wurple.presentation.flow.more.aboutapp.AboutAppFragment
import com.wurple.presentation.flow.more.account.AccountFragment
import com.wurple.presentation.flow.more.account.createdeleteaccountrequest.CreateDeleteAccountRequestFragment
import com.wurple.presentation.flow.more.account.deleteaccount.DeleteAccountFragment
import com.wurple.presentation.flow.more.account.emailverification.DeleteAccountEmailVerificationFragment
import com.wurple.presentation.flow.more.invitefriend.InviteFriendFragment
import com.wurple.presentation.flow.more.support.SupportFragment
import com.wurple.presentation.flow.previews.add.PreviewsAddFragment
import com.wurple.presentation.flow.previews.preview.PreviewFragment
import com.wurple.presentation.flow.previews.temppreview.TempPreviewFragment
import com.wurple.presentation.flow.onboarding.OnboardingFragment
import com.wurple.presentation.flow.profile.edit.EditProfileFragment
import com.wurple.presentation.flow.profile.edit.bio.EditProfileBioFragment
import com.wurple.presentation.flow.profile.edit.dob.EditProfileDobFragment
import com.wurple.presentation.flow.profile.edit.education.EditProfileEducationFragment
import com.wurple.presentation.flow.profile.edit.education.add.EditProfileEducationAddFragment
import com.wurple.presentation.flow.profile.edit.email.EditProfileEmailFragment
import com.wurple.presentation.flow.profile.edit.email.createeditprofileemailrequest.CreateEditProfileEmailRequestFragment
import com.wurple.presentation.flow.profile.edit.email.emailverification.EditProfileEmailEmailVerificationFragment
import com.wurple.presentation.flow.profile.edit.experience.EditProfileExperienceFragment
import com.wurple.presentation.flow.profile.edit.experience.add.EditProfileExperienceAddFragment
import com.wurple.presentation.flow.profile.edit.image.EditProfileImageFragment
import com.wurple.presentation.flow.profile.edit.languages.EditProfileLanguagesFragment
import com.wurple.presentation.flow.profile.edit.languages.add.EditProfileLanguagesAddFragment
import com.wurple.presentation.flow.profile.edit.location.EditProfileLocationFragment
import com.wurple.presentation.flow.profile.edit.name.EditProfileNameFragment
import com.wurple.presentation.flow.profile.edit.skills.EditProfileSkillsFragment
import com.wurple.presentation.flow.profile.edit.skills.add.EditProfileSkillsAddFragment
import com.wurple.presentation.flow.profile.edit.username.EditProfileUsernameFragment

fun getFragmentByScreen(screen: Screen): BaseFragment {
    return when (screen) {
        is Screen.SignIn -> SignInFragment.newInstance(screen.deepLink)
        is Screen.SignInEmailVerification -> SignInEmailVerificationFragment.newInstance(
            screen.email
        )
        is Screen.Main -> MainFragment.newInstance(screen.deepLink)
        Screen.Onboarding -> OnboardingFragment.newInstance()

        Screen.EditProfile -> EditProfileFragment.newInstance()
        Screen.EditProfileImage -> EditProfileImageFragment.newInstance()
        Screen.EditProfileName -> EditProfileNameFragment.newInstance()
        Screen.EditProfileDob -> EditProfileDobFragment.newInstance()
        Screen.EditProfileLocation -> EditProfileLocationFragment.newInstance()
        Screen.EditProfileEmail -> EditProfileEmailFragment.newInstance()
        Screen.CreateEditProfileEmailRequest -> CreateEditProfileEmailRequestFragment.newInstance()
        Screen.EditProfileEmailEmailVerification -> EditProfileEmailEmailVerificationFragment.newInstance()
        Screen.EditProfileUsername -> EditProfileUsernameFragment.newInstance()
        Screen.EditProfileBio -> EditProfileBioFragment.newInstance()
        Screen.EditProfileSkills -> EditProfileSkillsFragment.newInstance()
        is Screen.EditProfileSkillsAdd -> EditProfileSkillsAddFragment.newInstance(
            screen.skillId
        )
        Screen.EditProfileExperience -> EditProfileExperienceFragment.newInstance()
        is Screen.EditProfileExperienceAdd -> EditProfileExperienceAddFragment.newInstance(
            screen.experienceId
        )
        Screen.EditProfileEducation -> EditProfileEducationFragment.newInstance()
        is Screen.EditProfileEducationAdd -> EditProfileEducationAddFragment.newInstance(
            screen.educationId
        )
        Screen.EditProfileLanguages -> EditProfileLanguagesFragment.newInstance()
        is Screen.EditProfileLanguagesAdd -> EditProfileLanguagesAddFragment.newInstance(
            screen.languageId,
            screen.languageLevelId
        )

        Screen.Account -> AccountFragment.newInstance()
        Screen.CreateDeleteAccountRequest -> CreateDeleteAccountRequestFragment.newInstance()
        Screen.DeleteAccountEmailVerification -> DeleteAccountEmailVerificationFragment.newInstance()
        Screen.DeleteAccount -> DeleteAccountFragment.newInstance()
        Screen.Support -> SupportFragment.newInstance()
        Screen.InviteFriend -> InviteFriendFragment.newInstance()
        Screen.AboutApp -> AboutAppFragment.newInstance()

        is Screen.PreviewsAdd -> PreviewsAddFragment.newInstance(screen.previewId)
        is Screen.TempPreview -> TempPreviewFragment.newInstance()
        is Screen.Preview -> PreviewFragment.newInstance(screen.previewId)
    }
}
