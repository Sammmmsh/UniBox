package com.example.unibox.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.net.InetAddress
import javax.inject.Inject

class PublicWebUrlValidator @Inject constructor() {
    suspend fun sanitizeIfPublic(url: String): String? = withContext(Dispatchers.IO) {
        val parsedUrl = parseUrl(url) ?: return@withContext null
        val host = parsedUrl.host
        if (host.isObviouslyLocal()) return@withContext null

        val addresses = InetAddress.getAllByName(host)
        if (addresses.isEmpty() || addresses.any { !it.isPublicWebAddress() }) {
            return@withContext null
        }
        sanitizeTrackingParameters(parsedUrl.toString())
    }

    internal fun isStructurallyPublic(url: String): Boolean {
        val parsedUrl = parseUrl(url) ?: return false
        return !parsedUrl.host.isObviouslyLocal()
    }

    internal fun sanitizeTrackingParameters(url: String): String? {
        val parsedUrl = parseUrl(url) ?: return null
        return parsedUrl.newBuilder()
            .fragment(null)
            .apply {
                parsedUrl.queryParameterNames
                    .filter { it.lowercase() in TRACKING_PARAMETERS }
                    .forEach(::removeAllQueryParameters)
            }
            .build()
            .toString()
    }

    private fun parseUrl(url: String): HttpUrl? {
        val parsed = url.trim().toHttpUrlOrNull() ?: return null
        if (parsed.scheme !in setOf("http", "https")) return null
        if (parsed.username.isNotBlank() || parsed.password.isNotBlank()) return null
        return parsed
    }

    private fun String.isObviouslyLocal(): Boolean {
        return this == "localhost" ||
            this == "0.0.0.0" ||
            this == "::1" ||
            endsWith(".local") ||
            endsWith(".internal") ||
            endsWith(".lan") ||
            isPrivateIpv4()
    }

    private fun String.isPrivateIpv4(): Boolean {
        val parts = split('.').mapNotNull(String::toIntOrNull)
        if (parts.size != 4 || parts.any { it !in 0..255 }) return false
        return parts[0] == 10 ||
            parts[0] == 127 ||
            parts[0] == 0 ||
            parts[0] == 169 && parts[1] == 254 ||
            parts[0] == 172 && parts[1] in 16..31 ||
            parts[0] == 192 && parts[1] == 168
    }

    private companion object {
        val TRACKING_PARAMETERS = setOf(
            "fbclid",
            "gclid",
            "dclid",
            "msclkid",
            "mc_cid",
            "mc_eid",
            "_hsenc",
            "_hsmi",
            "utm_source",
            "utm_medium",
            "utm_campaign",
            "utm_term",
            "utm_content",
            "utm_id"
        )
    }
}
