package com.wurple.presentation.flow.more.aboutapp

import androidx.lifecycle.MutableLiveData
import com.wurple.data.Wurple
import com.wurple.domain.AppInfo
import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class AboutAppViewModel(
    private val wurple: Wurple,
    private val appInfo: AppInfo,
    logger: Logger
) : BaseViewModel(logger) {
    val rateAppLiveData = SingleLiveEvent<String>()
    val privacyPolicyLiveData = SingleLiveEvent<String>()
    val termsAndConditionsLiveData = SingleLiveEvent<String>()
    val appVersionNameLiveData = MutableLiveData(appInfo.version)
    val appPackageNameLiveData = MutableLiveData(appInfo.packageName)

    fun navigateBack() {
        navigateBackward()
    }

    fun navigateOnboarding() {
        navigateForwardTo(Screen.Onboarding)
    }

    fun navigateRateApp() {
        rateAppLiveData.value = appInfo.packageName
    }

    fun navigatePrivacyPolicy() {
        privacyPolicyLiveData.value = wurple.privacyPolicyUrl
    }

    fun navigateTermsAndConditions() {
        termsAndConditionsLiveData.value = wurple.termsAndConditionsUrl
    }
}