package com.wurple.data.di

import com.wurple.domain.dispatcher.DefaultDispatcherProvider
import com.wurple.domain.dispatcher.DispatcherProvider
import com.wurple.domain.usecase.account.DeleteAccountUseCase
import com.wurple.domain.usecase.auth.*
import com.wurple.domain.usecase.deeplink.GetInviteFriendDeepLinkUseCase
import com.wurple.domain.usecase.deeplink.GetPreviewDeepLinkUseCase
import com.wurple.domain.usecase.deeplink.HandleDeepLinkUseCase
import com.wurple.domain.usecase.info.GetFillOutProfileUseCase
import com.wurple.domain.usecase.info.SetFillOutProfileAsShownUseCase
import com.wurple.domain.usecase.language.GetLanguageLevelsUseCase
import com.wurple.domain.usecase.language.GetLanguagesUseCase
import com.wurple.domain.usecase.location.GetLocationByIdUseCase
import com.wurple.domain.usecase.location.LocationSearchUseCase
import com.wurple.domain.usecase.onboarding.GetOnboardingUseCase
import com.wurple.domain.usecase.onboarding.SetOnboardingUseCase
import com.wurple.domain.usecase.preview.*
import com.wurple.domain.usecase.user.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule: Module = module {
    // Dispatcher
    singleOf(::DefaultDispatcherProvider) { bind<DispatcherProvider>() }
    // Auth
    factory {
        RequestSignInUseCase(
            authGateway = get(),
            emailInputValidationRule = get(emailInputValidationRuleQualifier),
            inputValidator = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        RequestDeleteAccountUseCase(
            authGateway = get(),
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        RequestEditProfileEmailUseCase(
            authGateway = get(),
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        SignInUseCase(
            clearCacheUseCase = get(),
            deepLinkManager = get(),
            authGateway = get(),
            userGateway = get(),
            logger = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        SignOutUseCase(
            clearCacheUseCase = get(),
            authGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        ClearCacheUseCase(
            authGateway = get(),
            userGateway = get(),
            onboardingGateway = get(),
            infoGateway = get(),
            previewGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    // Onboarding
    factory {
        GetOnboardingUseCase(
            onboardingGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        SetOnboardingUseCase(
            onboardingGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    // Info
    factory {
        GetFillOutProfileUseCase(
            infoGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        SetFillOutProfileAsShownUseCase(
            infoGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    // Account
    factory {
        DeleteAccountUseCase(
            clearCacheUseCase = get(),
            userGateway = get(),
            accountGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    // User
    factory {
        GetCurrentUserUseCase(
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        FetchCurrentUserUseCase(
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        ObserveCurrentUserUseCase(
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        GetCurrentUserImageFileUseCase(
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        UpdateCurrentUserUseCase(
            firstNameInputValidationRule = get(firstNameInputValidationRuleQualifier),
            lastNameInputValidationRule = get(lastNameInputValidationRuleQualifier),
            dobInputValidationRule = get(dobInputValidationRuleQualifier),
            usernameInputValidationRule = get(usernameInputValidationRuleQualifier),
            bioInputValidationRule = get(bioInputValidationRuleQualifier),
            inputValidator = get(),
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        UpdateCurrentUserEmailUseCase(
            emailInputValidationRule = get(emailInputValidationRuleQualifier),
            inputValidator = get(),
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        UpdateCurrentUserSkillsUseCase(
            skillInputValidationRule = get(skillInputValidationRuleQualifier),
            inputValidator = get(),
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        UpdateCurrentUserExperienceUseCase(
            positionInputValidationRule = get(positionInputValidationRuleQualifier),
            companyNameInputValidationRule = get(companyNameInputValidationRuleQualifier),
            positionDateFromInputValidationRule = get(positionDateFromInputValidationRuleQualifier),
            positionDateToInputValidationRule = get(positionDateToInputValidationRuleQualifier),
            inputValidator = get(),
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        UpdateCurrentUserEducationUseCase(
            degreeInputValidationRule = get(degreeInputValidationRuleQualifier),
            institutionInputValidationRule = get(institutionInputValidationRuleQualifier),
            graduationDateFromInputValidationRule = get(graduationDateInputValidationRuleQualifier),
            inputValidator = get(),
            userGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        UpdateCurrentUserLocationUseCase(
            userGateway = get(),
            locationManager = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    // Location
    factory {
        LocationSearchUseCase(
            locationManager = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        GetLocationByIdUseCase(
            locationManager = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    // Deep link
    factory {
        GetInviteFriendDeepLinkUseCase(
            userGateway = get(),
            deepLinkManager = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        GetPreviewDeepLinkUseCase(
            deepLinkManager = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        HandleDeepLinkUseCase(
            deepLinkManager = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    // Language
    factory {
        GetLanguagesUseCase(
            languageGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        GetLanguageLevelsUseCase(
            languageGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    // Preview
    factory {
        ObservePreviewUseCase(
            previewGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        FetchPreviewUseCase(
            userGateway = get(),
            previewGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        ObserveUserPreviewUseCase(
            observeCurrentUserUseCase = get(),
            observePreviewUseCase = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        FetchUserPreviewUseCase(
            fetchCurrentUserUseCase = get(),
            fetchPreviewUseCase = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        GetPreviewUseCase(
            userGateway = get(),
            previewGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        DeletePreviewUseCase(
            userGateway = get(),
            previewGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        GetTempPreviewUseCase(
            userGateway = get(),
            previewGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        SetTempPreviewUseCase(
            previewTitleInputValidationRule = get(previewTitleInputValidationRuleQualifier),
            inputValidator = get(),
            previewGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        CreatePreviewUseCase(
            previewTitleInputValidationRule = get(previewTitleInputValidationRuleQualifier),
            inputValidator = get(),
            userGateway = get(),
            previewGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
    factory {
        UpdatePreviewUseCase(
            previewTitleInputValidationRule = get(previewTitleInputValidationRuleQualifier),
            inputValidator = get(),
            userGateway = get(),
            previewGateway = get(),
            coroutineContext = get<DispatcherProvider>().io,
            errorHandler = get()
        )
    }
}
