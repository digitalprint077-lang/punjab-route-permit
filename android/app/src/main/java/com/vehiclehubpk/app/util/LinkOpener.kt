package com.vehiclehubpk.app.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.vehiclehubpk.app.R

sealed class OpenLinkResult {
    data object Opened : OpenLinkResult()
    data object Offline : OpenLinkResult()
    data object InvalidUrl : OpenLinkResult()
    data object NoApp : OpenLinkResult()
}

object LinkOpener {
    fun open(context: Context, url: String): OpenLinkResult {
        val trimmed = url.trim()
        if (!URLUtil.isNetworkUrl(trimmed) &&
            !trimmed.startsWith("http://") &&
            !trimmed.startsWith("https://")
        ) {
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

        // Prefer Custom Tabs, then fall back to any VIEW handler.
        if (launchCustomTab(context, uri)) {
            return OpenLinkResult.Opened
        }
        return launchBrowser(context, uri)
    }

    private fun launchCustomTab(context: Context, uri: Uri): Boolean {
        return try {
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
            if (context !is Activity) {
                customTabs.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            customTabs.launchUrl(context, uri)
            true
        } catch (_: ActivityNotFoundException) {
            false
        } catch (_: Exception) {
            false
        }
    }

    private fun launchBrowser(context: Context, uri: Uri): OpenLinkResult {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
            OpenLinkResult.Opened
        } catch (_: ActivityNotFoundException) {
            OpenLinkResult.NoApp
        } catch (_: Exception) {
            OpenLinkResult.NoApp
        }
    }
}
