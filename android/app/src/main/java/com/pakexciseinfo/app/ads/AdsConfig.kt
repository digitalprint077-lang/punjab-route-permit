package com.pakexciseinfo.app.ads

/**
 * AdMob configuration.
 *
 * Real ads often return NO_FILL until:
 * - AdMob payment/account verification is complete
 * - App is published on Play Store and linked in AdMob
 * - App status in AdMob shows Ready (can take 1–3 days after linking)
 *
 * To confirm the banner code works, temporarily set [useTestAds] = true.
 */
object AdsConfig {
    /** Master switch for showing ads in the UI. */
    const val enabled: Boolean = true

    /**
     * true  = Google sample test ads (dev only — never ship to Play Store)
     * false = real AdMob unit IDs below (required for Play Store release)
     */
    const val useTestAds: Boolean = false

    // ---- Real AdMob IDs (Vehicle Hub PK) ----
    const val realAppId: String = "ca-app-pub-7023406601971230~3121629531"
    const val realBannerUnitId: String = "ca-app-pub-7023406601971230/4027517096"

    // Google official test IDs
    private const val testAppId: String = "ca-app-pub-3940256099942544~3347511713"
    private const val testBannerUnitId: String = "ca-app-pub-3940256099942544/6300978111"

    val appId: String
        get() = if (useTestAds) testAppId else realAppId

    val bannerUnitId: String
        get() = if (useTestAds) testBannerUnitId else realBannerUnitId
}
