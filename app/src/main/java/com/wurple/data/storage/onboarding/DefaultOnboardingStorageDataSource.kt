package com.wurple.data.storage.onboarding

import com.wurple.data.datastore.OnboardingDataStore

class DefaultOnboardingStorageDataSource(
    private val onboardingDataStore: OnboardingDataStore
) : OnboardingStorageDataSource {
    override suspend fun shouldShowOnboarding(): Boolean {
        return onboardingDataStore.shouldShowOnboarding()
    }

    override suspend fun setOnboardingAsShown() {
        onboardingDataStore.setOnboardingAsShown()
    }

    override suspend fun clearCache() {
        onboardingDataStore.clearDataStore()
    }
}
