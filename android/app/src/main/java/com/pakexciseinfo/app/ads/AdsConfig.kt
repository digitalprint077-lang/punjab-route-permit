package com.pakexciseinfo.app.ads

/**
 * AdMob configuration.
 *
 * Ads are off. Google sample IDs are kept only as placeholders for the
 * AndroidManifest App ID requirement and for local testing if re-enabled.
 * Do not commit real publisher / unit IDs here.
 */
object AdsConfig {
    /** Master switch for showing ads in the UI. */
    const val enabled: Boolean = false

    /**
     * true  = Google sample test ads (dev only — never ship to Play Store)
     * false = unused while [enabled] is false; set real IDs before shipping ads
     */
    const val useTestAds: Boolean = true

    // Google official sample IDs only (no real publisher IDs in the app)
    private const val testAppId: String = "ca-app-pub-3940256099942544~3347511713"
    private const val testBannerUnitId: String = "ca-app-pub-3940256099942544/6300978111"

    val appId: String
        get() = testAppId

    val bannerUnitId: String
        get() = testBannerUnitId
}
