package com.wurple.data.di

import com.wurple.data.DefaultAppInfo
import com.wurple.data.Wurple
import com.wurple.data.datastore.AuthDataStore
import com.wurple.data.datastore.OnboardingDataStore
import com.wurple.data.datastore.InfoDataStore
import com.wurple.data.date.DataDateManager
import com.wurple.data.deeplink.DataDeepLinkManager
import com.wurple.data.education.DataUserEducationManager
import com.wurple.data.error.DataErrorHandler
import com.wurple.data.experience.DataUserExperienceManager
import com.wurple.data.firebase.FirebaseManager
import com.wurple.data.gateway.*
import com.wurple.data.language.DataUserLanguageManager
import com.wurple.data.location.DefaultLocationManager
import com.wurple.data.log.DefaultLogger
import com.wurple.data.model.mapper.education.FromUserEducationDataToUserEducationMapper
import com.wurple.data.model.mapper.education.FromUserEducationToUserEducationDataMapper
import com.wurple.data.model.mapper.experience.FromUserExperienceDataToUserExperienceMapper
import com.wurple.data.model.mapper.experience.FromUserExperienceToUserExperienceDataMapper
import com.wurple.data.model.mapper.language.FromLanguageDataToLanguageMapper
import com.wurple.data.model.mapper.language.FromLanguageLevelDataToLanguageLevelMapper
import com.wurple.data.model.mapper.language.FromUserLanguageToUserLanguageDataMapper
import com.wurple.data.model.mapper.preview.FromPreviewRemoteDataToPreviewMapper
import com.wurple.data.model.mapper.preview.FromPreviewStorageDataToPreviewMapper
import com.wurple.data.model.mapper.preview.FromPreviewToPreviewStorageDataMapper
import com.wurple.data.model.mapper.skills.FromUserSkillsDataToUserSkillsMapper
import com.wurple.data.model.mapper.skills.FromUserSkillsToUserSkillsDataMapper
import com.wurple.data.model.mapper.user.FromUserRemoteDataToUserMapper
import com.wurple.data.model.mapper.user.FromUserStorageDataToUserMapper
import com.wurple.data.model.mapper.user.FromUserToUserStorageDataMapper
import com.wurple.data.remote.account.AccountRemoteDataSource
import com.wurple.data.remote.account.DefaultAccountRemoteDataSource
import com.wurple.data.remote.auth.AuthRemoteDataSource
import com.wurple.data.remote.auth.DefaultAuthRemoteDataSource
import com.wurple.data.remote.preview.DefaultPreviewRemoteDataSource
import com.wurple.data.remote.preview.PreviewRemoteDataSource
import com.wurple.data.remote.user.DefaultUserRemoteDataSource
import com.wurple.data.remote.user.UserRemoteDataSource
import com.wurple.data.skills.DataUserSkillsManager
import com.wurple.data.storage.auth.AuthStorageDataSource
import com.wurple.data.storage.auth.DefaultAuthStorageDataSource
import com.wurple.data.storage.onboarding.OnboardingStorageDataSource
import com.wurple.data.storage.onboarding.DefaultOnboardingStorageDataSource
import com.wurple.data.storage.info.InfoStorageDataSource
import com.wurple.data.storage.info.DefaultInfoStorageDataSource
import com.wurple.data.storage.language.DefaultLanguageStorageDataSource
import com.wurple.data.storage.language.LanguageStorageDataSource
import com.wurple.data.storage.preview.DefaultPreviewStorageDataSource
import com.wurple.data.storage.preview.PreviewStorageDataSource
import com.wurple.data.storage.room.WurpleDatabase
import com.wurple.data.storage.user.DefaultUserStorageDataSource
import com.wurple.data.storage.user.UserStorageDataSource
import com.wurple.data.validation.DefaultInputValidator
import com.wurple.data.validation.credentials.EmailInputValidationRule
import com.wurple.data.validation.preview.PreviewTitleInputValidationRule
import com.wurple.data.validation.user.*
import com.wurple.domain.AppInfo
import com.wurple.domain.date.DateManager
import com.wurple.domain.deeplink.DeepLinkManager
import com.wurple.domain.education.UserEducationManager
import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.experience.UserExperienceManager
import com.wurple.domain.gateway.*
import com.wurple.domain.language.UserLanguageManager
import com.wurple.domain.location.LocationManager
import com.wurple.domain.log.Logger
import com.wurple.domain.skills.UserSkillsManager
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.InputValidator
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule: Module = module {
    // App
    singleOf(::DefaultAppInfo) { bind<AppInfo>() }
    singleOf(::Wurple)

    // Database
    singleOf(WurpleDatabase::provide)

    // Logger
    singleOf(::DefaultLogger) {
        bind<Logger>()
        createdAtStart()
    }

    // Error
    singleOf(::DataErrorHandler) { bind<ErrorHandler>() }

    // Date
    singleOf(::DataDateManager) { bind<DateManager>() }

    // Deep link
    singleOf(::DataDeepLinkManager) { bind<DeepLinkManager>() }

    // Location
    singleOf(::DefaultLocationManager) { bind<LocationManager>() }

    // Skills
    singleOf(::DataUserSkillsManager) { bind<UserSkillsManager>() }

    // Experience
    singleOf(::DataUserExperienceManager) { bind<UserExperienceManager>() }

    // Education
    singleOf(::DataUserEducationManager) { bind<UserEducationManager>() }

    // Language
    singleOf(::DefaultLanguageStorageDataSource) { bind<LanguageStorageDataSource>() }
    singleOf(::DefaultLanguageGateway) { bind<LanguageGateway>() }
    singleOf(::DataUserLanguageManager) { bind<UserLanguageManager>() }

    // Validation
    singleOf(::DefaultInputValidator) { bind<InputValidator>() }
    single<InputValidationRule<String>>(emailInputValidationRuleQualifier) {
        EmailInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(firstNameInputValidationRuleQualifier) {
        FirstNameInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(lastNameInputValidationRuleQualifier) {
        LastNameInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(dobInputValidationRuleQualifier) {
        DobInputValidationRule(
            context = get(),
            dateManager = get()
        )
    }
    single<InputValidationRule<String>>(usernameInputValidationRuleQualifier) {
        UsernameInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(bioInputValidationRuleQualifier) {
        BioInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(positionInputValidationRuleQualifier) {
        PositionInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(skillInputValidationRuleQualifier) {
        SkillInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(companyNameInputValidationRuleQualifier) {
        CompanyNameInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(positionDateFromInputValidationRuleQualifier) {
        PositionDateFromInputValidationRule(
            context = get(),
            dateManager = get()
        )
    }
    single<InputValidationRule<String>>(positionDateToInputValidationRuleQualifier) {
        PositionDateToInputValidationRule(
            context = get(),
            dateManager = get()
        )
    }
    single<InputValidationRule<String>>(degreeInputValidationRuleQualifier) {
        DegreeInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(institutionInputValidationRuleQualifier) {
        InstitutionInputValidationRule(context = get())
    }
    single<InputValidationRule<String>>(graduationDateInputValidationRuleQualifier) {
        GraduationDateInputValidationRule(
            context = get(),
            dateManager = get()
        )
    }
    single<InputValidationRule<String>>(previewTitleInputValidationRuleQualifier) {
        PreviewTitleInputValidationRule(context = get())
    }

    // Mapper
    singleOf(::FromUserRemoteDataToUserMapper)
    singleOf(::FromUserToUserStorageDataMapper)
    singleOf(::FromUserStorageDataToUserMapper)
    singleOf(::FromUserSkillsDataToUserSkillsMapper)
    singleOf(::FromUserSkillsToUserSkillsDataMapper)
    singleOf(::FromUserExperienceDataToUserExperienceMapper)
    singleOf(::FromUserExperienceToUserExperienceDataMapper)
    singleOf(::FromUserEducationDataToUserEducationMapper)
    singleOf(::FromUserEducationToUserEducationDataMapper)
    singleOf(::FromLanguageDataToLanguageMapper)
    singleOf(::FromLanguageLevelDataToLanguageLevelMapper)
    singleOf(::FromUserLanguageToUserLanguageDataMapper)
    singleOf(::FromPreviewToPreviewStorageDataMapper)
    singleOf(::FromPreviewStorageDataToPreviewMapper)
    singleOf(::FromPreviewRemoteDataToPreviewMapper)

    // Firebase
    singleOf(::FirebaseManager)

    // Auth
    singleOf(::AuthDataStore)
    singleOf(::DefaultAuthStorageDataSource) { bind<AuthStorageDataSource>() }
    singleOf(::DefaultAuthRemoteDataSource) { bind<AuthRemoteDataSource>() }
    singleOf(::DefaultAuthGateway) { bind<AuthGateway>() }

    // Onboarding
    singleOf(::OnboardingDataStore)
    singleOf(::DefaultOnboardingStorageDataSource) { bind<OnboardingStorageDataSource>() }
    singleOf(::DefaultOnboardingGateway) { bind<OnboardingGateway>() }

    // Info
    singleOf(::InfoDataStore)
    singleOf(::DefaultInfoStorageDataSource) { bind<InfoStorageDataSource>() }
    singleOf(::DefaultInfoGateway) { bind<InfoGateway>() }

    // Account
    singleOf(::DefaultAccountRemoteDataSource) { bind<AccountRemoteDataSource>() }
    singleOf(::DefaultAccountGateway) { bind<AccountGateway>() }

    // User
    single<UserStorageDataSource> {
        DefaultUserStorageDataSource(
            context = get(),
            firebaseManager = get(),
            userDao = get<WurpleDatabase>().userDao(),
            fromUserToUserStorageDataMapper = get(),
            fromUserStorageDataToUserMapper = get(),
            dispatcherProvider = get()
        )
    }
    singleOf(::DefaultUserRemoteDataSource) { bind<UserRemoteDataSource>() }
    singleOf(::DefaultUserGateway) { bind<UserGateway>() }

    // Preview
    single<PreviewStorageDataSource> {
        DefaultPreviewStorageDataSource(
            context = get(),
            previewDao = get<WurpleDatabase>().previewDao(),
            fromPreviewToPreviewStorageDataMapper = get(),
            fromPreviewStorageDataToPreviewMapper = get(),
            dateManager = get()
        )
    }
    singleOf(::DefaultPreviewRemoteDataSource) { bind<PreviewRemoteDataSource>() }
    singleOf(::DefaultPreviewGateway) { bind<PreviewGateway>() }
}
