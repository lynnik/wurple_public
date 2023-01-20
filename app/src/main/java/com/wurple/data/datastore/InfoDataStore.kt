package com.wurple.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.first

class InfoDataStore(
    context: Context
) : PreferencesDataStore(
    context = context,
    name = "info_datastore"
) {
    suspend fun shouldShowFillOutProfile(): Boolean {
        return getData(KEY_FILL_OUT_PROFILE, true).first() ?: true
    }

    suspend fun setFillOutProfileAsShown() {
        setData(KEY_FILL_OUT_PROFILE, false)
    }

    companion object {
        private val KEY_FILL_OUT_PROFILE = booleanPreferencesKey("fillOutProfile")
    }
}
