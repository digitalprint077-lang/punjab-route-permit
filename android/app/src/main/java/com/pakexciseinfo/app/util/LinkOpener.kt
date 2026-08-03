package com.pakexciseinfo.app.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.pakexciseinfo.app.R

sealed class OpenLinkResult {
    data object Opened : OpenLinkResult()
    data object Offline : OpenLinkResult()
    data object InvalidUrl : OpenLinkResult()
    data object NoApp : OpenLinkResult()
}

object LinkOpener {
    fun open(context: Context, url: String): OpenLinkResult {
        val trimmed = url.trim()
        if (!URLUtil.isNetworkUrl(trimmed) && !trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            return OpenLinkResult.InvalidUrl
        }
        if (!NetworkMonitor.isOnline(context)) {
            return OpenLinkResult.Offline
        }

        val uri = runCatching { Uri.parse(trimmed) }.getOrNull()
            ?: return OpenLinkResult.InvalidUrl
        if (uri.scheme.isNullOrBlank() || uri.host.isNullOrBlank()) {
            return OpenLinkResult.InvalidUrl
        }

        val color = ContextCompat.getColor(context, R.color.brand_teal)
        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(color)
            .build()
        val customTabs = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(params)
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .setShareState(CustomTabsIntent.SHARE_STATE_ON)
            .build()

        return try {
            customTabs.launchUrl(context, uri)
            OpenLinkResult.Opened
        } catch (_: ActivityNotFoundException) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (intent.resolveActivity(context.packageManager) == null) {
                    OpenLinkResult.NoApp
                } else {
                    context.startActivity(intent)
                    OpenLinkResult.Opened
                }
            } catch (_: Exception) {
                OpenLinkResult.NoApp
            }
        } catch (_: Exception) {
            OpenLinkResult.NoApp
        }
    }
}
