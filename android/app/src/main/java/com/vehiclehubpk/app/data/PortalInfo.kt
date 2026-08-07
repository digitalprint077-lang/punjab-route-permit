package com.vehiclehubpk.app.data

import android.content.Context
import com.vehiclehubpk.app.R

/**
 * Intermediate information shown before opening an external government website
 * in Chrome Custom Tabs / the default browser (never inside a WebView).
 */
data class PortalInfo(
    val title: String,
    val authorityName: String,
    val officialUrl: String,
    val detail: String = "",
    /** Optional CTA override shown on the information page before leaving the app. */
    val openCta: String = "",
) {
    companion object {
        fun forGuide(guide: GuideItem, openCta: String = ""): PortalInfo = PortalInfo(
            title = guide.title,
            authorityName = guide.authorityName.ifBlank { "Official government website" },
            officialUrl = guide.officialUrl,
            detail = guide.description,
            openCta = openCta,
        )
    }
}

/** Specific “Open Official …” CTA for popular guide topics. */
fun guideOpenCta(context: Context, guideId: String): String = when (guideId) {
    "verify" -> context.getString(R.string.cta_open_vehicle_verification)
    "token" -> context.getString(R.string.cta_open_token_tax)
    "challan" -> context.getString(R.string.cta_open_echallan)
    "licence" -> context.getString(R.string.cta_open_driving_licence)
    "smartcard" -> context.getString(R.string.cta_open_smart_card)
    else -> ""
}

fun serviceOpenCta(context: Context, serviceId: String, title: String): String {
    val key = "$serviceId $title".lowercase()
    return when {
        "verify" in key || "mtmis" in key || "vehicle detail" in key || "vehicle search" in key ->
            context.getString(R.string.cta_open_vehicle_verification)
        "arms" in key -> ""
        "-dl" in serviceId || "dlims" in key || "dlmis" in key || "driving licence" in key ||
            "driving license" in key || key.contains(" dls") || key.endsWith("dls") ->
            context.getString(R.string.cta_open_driving_licence)
        "smart" in key || "vrc" in key ->
            context.getString(R.string.cta_open_smart_card)
        "challan" in key ->
            context.getString(R.string.cta_open_echallan)
        "token" in key || "tax" in key || "epay" in key || "e-pay" in key ->
            context.getString(R.string.cta_open_token_tax)
        else -> ""
    }
}

data class GovernmentSource(
    val region: String,
    val department: String,
    val websiteUrl: String,
)
