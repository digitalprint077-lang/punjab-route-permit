package com.pakexciseinfo.app.util

import android.app.Application
import android.util.Log
import com.pakexciseinfo.app.BuildConfig

/**
 * Optional Firebase Crashlytics bridge.
 * Enabled only when google-services.json is present and CRASHLYTICS_ENABLED=true.
 */
object CrashReporting {
    private const val TAG = "CrashReporting"

    fun init(app: Application) {
        if (!BuildConfig.CRASHLYTICS_ENABLED) {
            Log.i(TAG, "Crashlytics disabled (add app/google-services.json to enable).")
            return
        }
        runCatching {
            val firebaseAppClass = Class.forName("com.google.firebase.FirebaseApp")
            firebaseAppClass.getMethod("initializeApp", android.content.Context::class.java)
                .invoke(null, app)
            val crashlyticsClass = Class.forName("com.google.firebase.crashlytics.FirebaseCrashlytics")
            val instance = crashlyticsClass.getMethod("getInstance").invoke(null)
            crashlyticsClass.getMethod("setCrashlyticsCollectionEnabled", Boolean::class.javaPrimitiveType)
                .invoke(instance, true)
            Log.i(TAG, "Crashlytics enabled.")
        }.onFailure {
            Log.w(TAG, "Crashlytics init skipped: ${it.message}")
        }
    }

    fun record(throwable: Throwable) {
        if (!BuildConfig.CRASHLYTICS_ENABLED) return
        runCatching {
            val crashlyticsClass = Class.forName("com.google.firebase.crashlytics.FirebaseCrashlytics")
            val instance = crashlyticsClass.getMethod("getInstance").invoke(null)
            crashlyticsClass.getMethod("recordException", Throwable::class.java)
                .invoke(instance, throwable)
        }
    }
}
