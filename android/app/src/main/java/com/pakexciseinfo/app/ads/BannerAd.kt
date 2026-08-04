package com.pakexciseinfo.app.ads

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

private const val TAG = "BannerAd"
private const val MAX_RETRIES = 5
private const val RETRY_DELAY_MS = 15_000L

@Composable
fun BannerAd(
    modifier: Modifier = Modifier,
) {
    if (!AdsConfig.enabled) return

    val context = LocalContext.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val handler = remember { Handler(Looper.getMainLooper()) }
    val retryState = remember { intArrayOf(0) }
    var adLoaded by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            handler.removeCallbacksAndMessages(null)
        }
    }

    AndroidView(
        // Collapse empty slot so content isn't crushed above the tab bar
        modifier = modifier
            .fillMaxWidth()
            .height(if (adLoaded) 60.dp else 0.dp),
        factory = { ctx ->
            val activity = ctx.findActivity()
            val adContext = activity ?: ctx
            AdView(adContext).apply {
                val adSize = runCatching {
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(adContext, screenWidthDp)
                }.getOrElse { AdSize.BANNER }
                setAdSize(adSize)
                adUnitId = AdsConfig.bannerUnitId
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        retryState[0] = 0
                        adLoaded = true
                        Log.i(TAG, "Banner loaded (testAds=${AdsConfig.useTestAds})")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        adLoaded = false
                        Log.w(
                            TAG,
                            "Banner failed code=${error.code} domain=${error.domain} msg=${error.message}",
                        )
                        val attempt = retryState[0]
                        if (attempt >= MAX_RETRIES || !isAttachedToWindow) return
                        retryState[0] = attempt + 1
                        handler.postDelayed({
                            if (!isAttachedToWindow) return@postDelayed
                            val act = context.findActivity()
                            if (act != null && act.isFinishing) return@postDelayed
                            Log.i(TAG, "Retrying banner load #${retryState[0]}")
                            loadAd(AdRequest.Builder().build())
                        }, RETRY_DELAY_MS)
                    }
                }
                AdsManager.whenReady {
                    val act = adContext.findActivity()
                    if (act == null || !act.isFinishing) {
                        loadAd(AdRequest.Builder().build())
                    }
                }
            }
        },
        onRelease = { adView ->
            handler.removeCallbacksAndMessages(null)
            adView.destroy()
        },
    )
}

private fun Context.findActivity(): Activity? {
    var current: Context? = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}
