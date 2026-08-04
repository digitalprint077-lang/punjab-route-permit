package com.pakexciseinfo.app.ads

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import java.util.concurrent.atomic.AtomicBoolean

object AdsManager {
    private const val TAG = "AdsManager"
    private val ready = AtomicBoolean(false)
    private val pending = mutableListOf<() -> Unit>()

    fun init(app: Application) {
        if (!AdsConfig.enabled) return
        MobileAds.initialize(app) {
            Log.i(TAG, "AdMob ready (testAds=${AdsConfig.useTestAds}, unit=${AdsConfig.bannerUnitId})")
            ready.set(true)
            val runNow: List<() -> Unit>
            synchronized(pending) {
                runNow = pending.toList()
                pending.clear()
            }
            runNow.forEach { action ->
                runCatching { action() }
            }
        }
    }

    fun whenReady(action: () -> Unit) {
        if (!AdsConfig.enabled) return
        if (ready.get()) {
            action()
            return
        }
        synchronized(pending) {
            if (ready.get()) {
                action()
            } else {
                pending.add(action)
            }
        }
    }
}
