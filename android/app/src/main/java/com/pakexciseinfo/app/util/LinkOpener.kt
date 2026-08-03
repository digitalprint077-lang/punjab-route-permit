package com.pakexciseinfo.app.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.pakexciseinfo.app.R

object LinkOpener {
    fun open(context: Context, url: String) {
        val uri = Uri.parse(url)
        val color = ContextCompat.getColor(context, R.color.brand_navy)
        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(color)
            .build()
        val customTabs = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(params)
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .build()
        try {
            customTabs.launchUrl(context, uri)
        } catch (_: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}
