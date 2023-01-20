package com.wurple.data.gateway

import com.wurple.data.storage.onboarding.OnboardingStorageDataSource
import com.wurple.domain.gateway.OnboardingGateway

class DefaultOnboardingGateway(
    private val onboardingStorageDataSource: OnboardingStorageDataSource
) : OnboardingGateway {
    override suspend fun shouldShowOnboarding(): Boolean {
        return onboardingStorageDataSource.shouldShowOnboarding()
    }

    override suspend fun setOnboardingAsShown() {
        onboardingStorageDataSource.setOnboardingAsShown()
    }

    override suspend fun clearCache() {
        onboardingStorageDataSource.clearCache()
    }
}
