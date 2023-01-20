package com.wurple.data.log

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber

open class FirebaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority >= Log.ERROR) {
            Firebase.crashlytics.log("$tag: $message")
            if (t != null) {
                Firebase.crashlytics.recordException(t)
            }
        }
    }
}