package com.pakexciseinfo.app.data

import android.content.Context
import com.pakexciseinfo.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AppConfigRepository(private val context: Context) {

    suspend fun load(): AppConfig = withContext(Dispatchers.IO) {
        val bundled = readBundled()
        val remote = fetchRemote()
        when {
            remote == null -> bundled
            remote.version >= bundled.version -> remote
            else -> bundled.copy(urls = bundled.urls + remote.urls)
        }
    }

    private fun readBundled(): AppConfig {
        return runCatching {
            context.assets.open(ASSET_NAME).bufferedReader().use { parse(it.readText()) }
        }.getOrElse { AppConfig() }
    }

    private fun fetchRemote(): AppConfig? {
        return runCatching {
            val endpoint = BuildConfig.SITE_URL.trimEnd('/') + "/" + REMOTE_NAME
            val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                connectTimeout = 8_000
                readTimeout = 8_000
                requestMethod = "GET"
                instanceFollowRedirects = true
                setRequestProperty("Accept", "application/json")
            }
            connection.inputStream.bufferedReader().use { reader ->
                parse(reader.readText())
            }.also { connection.disconnect() }
        }.getOrNull()
    }

    private fun parse(raw: String): AppConfig {
        val root = JSONObject(raw)
        val urlsJson = root.optJSONObject("urls") ?: JSONObject()
        val urls = buildMap {
            val keys = urlsJson.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = urlsJson.optString(key).trim()
                if (value.isNotEmpty()) put(key, value)
            }
        }
        return AppConfig(
            version = root.optInt("version", 1),
            updatedAt = root.optString("updatedAt"),
            urls = urls,
        )
    }

    companion object {
        private const val ASSET_NAME = "app-config.json"
        private const val REMOTE_NAME = "app-config.json"
    }
}
