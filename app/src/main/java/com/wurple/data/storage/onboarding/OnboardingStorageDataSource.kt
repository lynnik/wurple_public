package com.wurple.data.storage.onboarding

interface OnboardingStorageDataSource {
    suspend fun shouldShowOnboarding(): Boolean
    suspend fun setOnboardingAsShown()
    suspend fun clearCache()
}
