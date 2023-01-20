package com.wurple.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class PreferencesDataStore(
    private val context: Context,
    name: String
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = name)

    suspend fun clearDataStore() {
        context.dataStore.edit { mutablePreferences -> mutablePreferences.clear() }
    }

    protected fun <T> getData(key: Preferences.Key<T>, defaultValue: T? = null): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    protected suspend fun <T> setData(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }
}
