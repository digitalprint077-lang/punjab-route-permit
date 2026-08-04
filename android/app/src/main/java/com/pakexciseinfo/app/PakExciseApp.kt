package com.pakexciseinfo.app

import android.app.Application
import com.pakexciseinfo.app.ads.AdsManager
import com.pakexciseinfo.app.util.CrashReporting

class PakExciseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CrashReporting.init(this)
        AdsManager.init(this)
    }
}
