package com.vehiclehubpk.app

import android.app.Application
import com.vehiclehubpk.app.ads.AdsManager
import com.vehiclehubpk.app.util.CrashReporting

class VehicleHubApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CrashReporting.init(this)
        AdsManager.init(this)
    }
}
