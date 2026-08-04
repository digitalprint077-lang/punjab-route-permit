package com.pakexciseinfo.app.data

data class AppConfig(
    val version: Int = 1,
    val updatedAt: String = "",
    val urls: Map<String, String> = emptyMap(),
) {
    fun urlFor(id: String, fallback: String): String {
        val override = urls[id]?.trim().orEmpty()
        return override.ifBlank { fallback }
    }
}
