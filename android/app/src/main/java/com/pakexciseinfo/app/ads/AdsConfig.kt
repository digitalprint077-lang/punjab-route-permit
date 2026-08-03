package com.pakexciseinfo.app.ads

/**
 * AdMob configuration.
 *
 * 1) Create an AdMob Android app for package com.pakexciseinfo.app
 * 2) Paste your real App ID + Banner unit ID below
 * 3) Set [useTestAds] = false before Play Store release
 *
 * Keep [enabled] = false if you want to ship without ads temporarily.
 */
object AdsConfig {
    /** Master switch for showing ads in the UI. */
    const val enabled: Boolean = true

    /**
     * true  = Google sample test ads (safe while developing)
     * false = your real AdMob unit IDs below
     */
    const val useTestAds: Boolean = false

    // ---- Real AdMob IDs (Pak Excise Info) ----
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
