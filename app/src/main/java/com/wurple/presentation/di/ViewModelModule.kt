package com.wurple.presentation.di

import android.os.Bundle
import com.wurple.domain.extension.empty
import com.wurple.presentation.extension.extract
import com.wurple.presentation.flow.auth.signin.SignInFragment
import com.wurple.presentation.flow.auth.signin.SignInViewModel
import com.wurple.presentation.flow.auth.signinemailverification.SignInEmailVerificationFragment
import com.wurple.presentation.flow.auth.signinemailverification.SignInEmailVerificationViewModel
import com.wurple.presentation.flow.main.MainFragment
import com.wurple.presentation.flow.main.MainViewModel
import com.wurple.presentation.flow.more.MoreViewModel
import com.wurple.presentation.flow.more.aboutapp.AboutAppViewModel
import com.wurple.presentation.flow.more.account.AccountViewModel
import com.wurple.presentation.flow.more.account.createdeleteaccountrequest.CreateDeleteAccountRequestViewModel
import com.wurple.presentation.flow.more.account.deleteaccount.DeleteAccountViewModel
import com.wurple.presentation.flow.more.account.emailverification.DeleteAccountEmailVerificationViewModel
import com.wurple.presentation.flow.more.invitefriend.InviteFriendViewModel
import com.wurple.presentation.flow.more.support.SupportViewModel
import com.wurple.presentation.flow.previews.PreviewsViewModel
import com.wurple.presentation.flow.previews.add.PreviewsAddFragment
import com.wurple.presentation.flow.previews.add.PreviewsAddViewModel
import com.wurple.presentation.flow.previews.preview.PreviewFragment
import com.wurple.presentation.flow.previews.preview.PreviewViewModel
import com.wurple.presentation.flow.previews.temppreview.TempPreviewViewModel
import com.wurple.presentation.flow.you.YouViewModel
import com.wurple.presentation.flow.onboarding.OnboardingViewModel
import com.wurple.presentation.flow.profile.edit.EditProfileViewModel
import com.wurple.presentation.flow.profile.edit.bio.EditProfileBioViewModel
import com.wurple.presentation.flow.profile.edit.dob.EditProfileDobViewModel
import com.wurple.presentation.flow.profile.edit.education.EditProfileEducationViewModel
import com.wurple.presentation.flow.profile.edit.education.add.EditProfileEducationAddFragment
import com.wurple.presentation.flow.profile.edit.education.add.EditProfileEducationAddViewModel
import com.wurple.presentation.flow.profile.edit.email.EditProfileEmailViewModel
import com.wurple.presentation.flow.profile.edit.email.createeditprofileemailrequest.CreateEditProfileEmailRequestViewModel
import com.wurple.presentation.flow.profile.edit.email.emailverification.EditProfileEmailEmailVerificationViewModel
import com.wurple.presentation.flow.profile.edit.experience.EditProfileExperienceViewModel
import com.wurple.presentation.flow.profile.edit.experience.add.EditProfileExperienceAddFragment
import com.wurple.presentation.flow.profile.edit.experience.add.EditProfileExperienceAddViewModel
import com.wurple.presentation.flow.profile.edit.image.EditProfileImageViewModel
import com.wurple.presentation.flow.profile.edit.languages.EditProfileLanguagesViewModel
import com.wurple.presentation.flow.profile.edit.languages.add.EditProfileLanguagesAddFragment
import com.wurple.presentation.flow.profile.edit.languages.add.EditProfileLanguagesAddViewModel
import com.wurple.presentation.flow.profile.edit.location.EditProfileLocationViewModel
import com.wurple.presentation.flow.profile.edit.name.EditProfileNameViewModel
import com.wurple.presentation.flow.profile.edit.skills.EditProfileSkillsViewModel
import com.wurple.presentation.flow.profile.edit.skills.add.EditProfileSkillsAddFragment
import com.wurple.presentation.flow.profile.edit.skills.add.EditProfileSkillsAddViewModel
import com.wurple.presentation.flow.profile.edit.username.EditProfileUsernameViewModel
import com.wurple.presentation.flow.root.RootViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule: Module = module {
    // Root
    viewModelOf(::RootViewModel)
    // Auth
    viewModel { (args: Bundle?) ->
        SignInViewModel(
            deepLink = args.extract(SignInFragment.EXTRA_DEEP_LINK, String.empty),
            wurple = get(),
            handleDeepLinkUseCase = get(),
            requestSignInUseCase = get(),
            logger = get()
        )
    }
    viewModel { (args: Bundle?) ->
        SignInEmailVerificationViewModel(
            email = args.extract(SignInEmailVerificationFragment.EXTRA_EMAIL, String.empty),
            requestSignInUseCase = get(),
            logger = get()
        )
    }
    // Main
    viewModel { (args: Bundle?) ->
        MainViewModel(
            deepLink = args.extract(MainFragment.EXTRA_DEEP_LINK, String.empty),
            handleDeepLinkUseCase = get(),
            logger = get()
        )
    }
    viewModelOf(::YouViewModel)
    viewModelOf(::PreviewsViewModel)
    // Onboarding
    viewModelOf(::OnboardingViewModel)
    // More
    viewModelOf(::MoreViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::CreateDeleteAccountRequestViewModel)
    viewModelOf(::DeleteAccountEmailVerificationViewModel)
    viewModelOf(::DeleteAccountViewModel)
    viewModelOf(::SupportViewModel)
    viewModelOf(::InviteFriendViewModel)
    viewModelOf(::AboutAppViewModel)
    // Edit profile
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::EditProfileNameViewModel)
    viewModelOf(::EditProfileImageViewModel)
    viewModelOf(::EditProfileDobViewModel)
    viewModelOf(::EditProfileLocationViewModel)
    viewModelOf(::EditProfileEmailViewModel)
    viewModelOf(::CreateEditProfileEmailRequestViewModel)
    viewModelOf(::EditProfileEmailEmailVerificationViewModel)
    viewModelOf(::EditProfileUsernameViewModel)
    viewModelOf(::EditProfileBioViewModel)
    viewModelOf(::EditProfileSkillsViewModel)
    viewModel { (args: Bundle?) ->
        EditProfileSkillsAddViewModel(
            skillId = args.extract(EditProfileSkillsAddFragment.EXTRA_SKILL_ID, String.empty),
            observeCurrentUserUseCase = get(),
            updateCurrentUserSkillsUseCase = get(),
            logger = get()
        )
    }
    viewModelOf(::EditProfileExperienceViewModel)
    viewModel { (args: Bundle?) ->
        EditProfileExperienceAddViewModel(
            experienceId = args.extract(
                EditProfileExperienceAddFragment.EXTRA_EXPERIENCE_ID,
                String.empty
            ),
            observeCurrentUserUseCase = get(),
            updateCurrentUserExperienceUseCase = get(),
            dateManager = get(),
            logger = get()
        )
    }
    viewModelOf(::EditProfileEducationViewModel)
    viewModel { (args: Bundle?) ->
        EditProfileEducationAddViewModel(
            educationId = args.extract(
                EditProfileEducationAddFragment.EXTRA_EDUCATION_ID,
                String.empty
            ),
            observeCurrentUserUseCase = get(),
            updateCurrentUserEducationUseCase = get(),
            dateManager = get(),
            logger = get()
        )
    }
    viewModelOf(::EditProfileLanguagesViewModel)
    viewModel { (args: Bundle?) ->
        EditProfileLanguagesAddViewModel(
            languageId = args.extract(
                EditProfileLanguagesAddFragment.EXTRA_LANGUAGE_ID,
                EditProfileLanguagesAddViewModel.LANGUAGE_DEFAULT_VALUE
            ),
            languageLevelId = args.extract(
                EditProfileLanguagesAddFragment.EXTRA_LANGUAGE_LEVEL_ID,
                EditProfileLanguagesAddViewModel.LANGUAGE_DEFAULT_VALUE
            ),
            getLanguagesUseCase = get(),
            getLanguageLevelsUseCase = get(),
            observeCurrentUserUseCase = get(),
            updateCurrentUserUseCase = get(),
            logger = get()
        )
    }
    // Previews
    viewModel { (args: Bundle?) ->
        PreviewsAddViewModel(
            previewId = args.extract(PreviewsAddFragment.EXTRA_PREVIEW_ID, String.empty),
            getPreviewUseCase = get(),
            createPreviewUseCase = get(),
            updatePreviewUseCase = get(),
            deletePreviewUseCase = get(),
            setTempPreviewUseCase = get(),
            fromUserTempPreviewToPreviewUiOnAddMapper = get(),
            dispatcherProvider = get(),
            logger = get()
        )
    }
    viewModel {
        TempPreviewViewModel(
            getTempPreviewUseCase = get(),
            fromUserTempPreviewToPreviewUiOnPreviewMapper = get(),
            dispatcherProvider = get(),
            logger = get()
        )
    }
    viewModel { (args: Bundle?) ->
        PreviewViewModel(
            previewId = args.extract(PreviewFragment.EXTRA_PREVIEW_ID, String.empty),
            dateManager = get(),
            getPreviewUseCase = get(),
            fromUserPreviewToPreviewUiMapper = get(),
            dispatcherProvider = get(),
            logger = get()
        )
    }
}
