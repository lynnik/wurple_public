package com.wurple.presentation.flow.onboarding.adapter

sealed class OnboardingPage {
    object First : OnboardingPage()
    object Second : OnboardingPage()
    object Third : OnboardingPage()
}
