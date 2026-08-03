package com.pakexciseinfo.app

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.pakexciseinfo.app.ads.AdsConfig
import com.pakexciseinfo.app.util.CrashReporting

class PakExciseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CrashReporting.init(this)
        if (AdsConfig.enabled) {
            MobileAds.initialize(this) {
                Log.i(TAG, "AdMob initialized (testAds=${AdsConfig.useTestAds})")
            }
        }
    }

    companion object {
        private const val TAG = "PakExciseApp"
    }
}
