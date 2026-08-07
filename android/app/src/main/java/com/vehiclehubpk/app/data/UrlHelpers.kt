package com.vehiclehubpk.app.data

import android.net.Uri

object UrlHelpers {
    /**
     * True for government-operated / government-designated hosts.
     * Non-government sites must not be labeled "Official source".
     */
    fun isGovernmentSource(url: String): Boolean {
        val host = runCatching { Uri.parse(url).host?.lowercase().orEmpty() }.getOrDefault("")
        if (host.isBlank()) return false
        if (host.endsWith(".gov.pk") || host.endsWith(".gob.pk") || host.endsWith(".gos.pk")) {
            return true
        }
        // Government-designated citizen portals (linked from official departments)
        val allowed = setOf(
            "dastakappecitizenkp.pk",
            "www.dastakappecitizenkp.pk",
            "islamabadpolice.gov.pk",
            "www.islamabadpolice.gov.pk",
            "gbp.gov.pk",
            "www.gbp.gov.pk",
            "qtp.gob.pk",
            "www.qtp.gob.pk",
        )
        return host in allowed || allowed.any { host.endsWith(".$it") }
    }
}
