package com.wurple.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.first

class OnboardingDataStore(
    context: Context
) : PreferencesDataStore(
    context = context,
    name = "onboarding_datastore"
) {
    suspend fun shouldShowOnboarding(): Boolean {
        return getData(KEY_ONBOARDING, true).first() ?: true
    }

    suspend fun setOnboardingAsShown() {
        setData(KEY_ONBOARDING, false)
    }

    companion object {
        private val KEY_ONBOARDING = booleanPreferencesKey("onboarding")
    }
}
