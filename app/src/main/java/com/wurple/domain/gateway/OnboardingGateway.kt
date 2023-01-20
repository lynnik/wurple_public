package com.wurple.domain.gateway

interface OnboardingGateway {
    suspend fun shouldShowOnboarding(): Boolean
    suspend fun setOnboardingAsShown()
    suspend fun clearCache()
}
