package com.example.unibox.data.remote

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Parsed OpenGraph metadata from a web page.
 */
data class OpenGraphData(
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val siteName: String? = null,
    val type: String? = null
)

/**
 * Fetches and parses OpenGraph meta tags from a URL using JSoup.
 * This runs on a background thread via WorkManager.
 */
@Singleton
class OpenGraphParser @Inject constructor() {

    companion object {
        private const val TIMEOUT_MS = 10_000
        private const val USER_AGENT =
            "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
    }

    fun parse(url: String): OpenGraphData {
        return try {
            val doc: Document = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MS)
                .followRedirects(true)
                .get()

            OpenGraphData(
                title = doc.selectOgTag("og:title")
                    ?: doc.selectMeta("twitter:title")
                    ?: doc.title().takeIf { it.isNotBlank() },
                description = doc.selectOgTag("og:description")
                    ?: doc.selectMeta("twitter:description")
                    ?: doc.selectMeta("description"),
                imageUrl = doc.selectOgTag("og:image")
                    ?: doc.selectMeta("twitter:image"),
                siteName = doc.selectOgTag("og:site_name"),
                type = doc.selectOgTag("og:type")
            )
        } catch (e: Exception) {
            // Network failures, timeouts, invalid URLs — return empty
            OpenGraphData()
        }
    }

    private fun Document.selectOgTag(property: String): String? {
        return select("meta[property=$property]").firstOrNull()?.attr("content")?.takeIf { it.isNotBlank() }
    }

    private fun Document.selectMeta(name: String): String? {
        return select("meta[name=$name]").firstOrNull()?.attr("content")?.takeIf { it.isNotBlank() }
    }
}
