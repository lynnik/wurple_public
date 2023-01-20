package com.wurple.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wurple.domain.extension.empty
import kotlinx.coroutines.flow.first

class AuthDataStore(
    context: Context
) : PreferencesDataStore(
    context = context,
    name = "auth_datastore"
) {
    suspend fun getSignInEmail(): String {
        return getData(KEY_SIGN_IN_EMAIL, String.empty).first() ?: String.empty
    }

    suspend fun setSignInEmail(email: String) {
        setData(KEY_SIGN_IN_EMAIL, email)
    }

    suspend fun getReferrerId(): String {
        return getData(KEY_REFERRER_ID, String.empty).first() ?: String.empty
    }

    suspend fun setReferrerId(referrerId: String) {
        setData(KEY_REFERRER_ID, referrerId)
    }

    companion object {
        private val KEY_SIGN_IN_EMAIL = stringPreferencesKey("signInEmail")
        private val KEY_REFERRER_ID = stringPreferencesKey("referrerId")
    }
}
