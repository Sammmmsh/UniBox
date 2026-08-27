package com.example.unibox.data.remote

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
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
    val type: String? = null,
    val canonicalUrl: String? = null,
    val author: String? = null,
    val publishedAt: String? = null,
    val language: String? = null
) {
    fun toWebPageData(): WebPageData = WebPageData(
        title = title,
        description = description,
        imageUrl = imageUrl,
        canonicalUrl = canonicalUrl,
        siteName = siteName,
        author = author,
        publishedAt = publishedAt,
        language = language
    )
}

/**
 * Fetches and parses OpenGraph meta tags from a URL using JSoup.
 * This runs on a background thread via WorkManager.
 */
@Singleton
class OpenGraphParser @Inject constructor(
    private val httpClient: OkHttpClient,
    private val urlValidator: PublicWebUrlValidator
) {

    companion object {
        private const val MAX_REDIRECTS = 5
        private const val MAX_HTML_BYTES = 2_000_000L
        private const val USER_AGENT =
            "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
    }

    suspend fun parse(url: String): OpenGraphData = withContext(Dispatchers.IO) {
        try {
            var currentUrl = urlValidator.sanitizeIfPublic(url)
                ?: return@withContext OpenGraphData()
            repeat(MAX_REDIRECTS + 1) {
                val request = Request.Builder()
                    .url(currentUrl)
                    .header("User-Agent", USER_AGENT)
                    .build()
                httpClient.newCall(request).execute().use { response ->
                    if (response.code in setOf(301, 302, 303, 307, 308)) {
                        val location = response.header("Location")
                            ?: return@withContext OpenGraphData()
                        val resolvedUrl = currentUrl.toHttpUrl().resolve(location)?.toString()
                            ?: return@withContext OpenGraphData()
                        currentUrl = urlValidator.sanitizeIfPublic(resolvedUrl)
                            ?: return@withContext OpenGraphData()
                    } else {
                        if (!response.isSuccessful) return@withContext OpenGraphData()
                        val body = response.body ?: return@withContext OpenGraphData()
                        val contentType = body.contentType()?.subtype
                        if (contentType != null && !contentType.contains("html")) {
                            return@withContext OpenGraphData()
                        }
                        val source = body.source()
                        source.request(MAX_HTML_BYTES + 1)
                        if (source.buffer.size > MAX_HTML_BYTES) return@withContext OpenGraphData()
                        return@withContext parseDocument(
                            Jsoup.parse(source.buffer.readUtf8(), currentUrl)
                        )
                    }
                }
            }
            OpenGraphData()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            OpenGraphData()
        }
    }

    private fun parseDocument(doc: Document): OpenGraphData = OpenGraphData(
        title = doc.selectOgTag("og:title")
            ?: doc.selectMeta("twitter:title")
            ?: doc.title().takeIf { it.isNotBlank() },
        description = doc.selectOgTag("og:description")
            ?: doc.selectMeta("twitter:description")
            ?: doc.selectMeta("description"),
        imageUrl = (doc.selectOgTag("og:image")
            ?: doc.selectMeta("twitter:image"))?.let {
            doc.baseUri().toHttpUrl().resolve(it)?.toString()
        },
        siteName = doc.selectOgTag("og:site_name"),
        type = doc.selectOgTag("og:type"),
        canonicalUrl = doc.select("link[rel=canonical]").firstOrNull()
            ?.absUrl("href")?.takeIf(String::isNotBlank),
        author = doc.selectMeta("author")
            ?: doc.selectOgTag("article:author"),
        publishedAt = doc.selectOgTag("article:published_time")
            ?: doc.selectMeta("date"),
        language = doc.select("html[lang]").firstOrNull()?.attr("lang")
            ?.takeIf(String::isNotBlank)
    )

    private fun Document.selectOgTag(property: String): String? {
        return select("meta[property=$property]").firstOrNull()?.attr("content")?.takeIf { it.isNotBlank() }
    }

    private fun Document.selectMeta(name: String): String? {
        return select("meta[name=$name]").firstOrNull()?.attr("content")?.takeIf { it.isNotBlank() }
    }
}
