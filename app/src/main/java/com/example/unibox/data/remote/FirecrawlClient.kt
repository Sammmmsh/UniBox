package com.example.unibox.data.remote

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
class FirecrawlClient @Inject constructor(
    private val httpClient: OkHttpClient
) {
    internal fun createRequest(url: String, apiKey: String? = null): Request {
        val payload = JSONObject()
            .put("url", url)
            .put("formats", JSONArray().put("markdown"))
            .put("onlyMainContent", true)
            .put("maxAge", CACHE_MAX_AGE_MS)
            .put("removeBase64Images", true)

        return Request.Builder()
            .url(SCRAPE_ENDPOINT)
            .header("Accept", "application/json")
            .header("User-Agent", "UniBox/1.0 (Android)")
            .apply {
                apiKey?.takeIf(String::isNotBlank)?.let { header("Authorization", "Bearer $it") }
            }
            .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
            .build()
    }

    suspend fun scrape(url: String, apiKey: String? = null): FirecrawlScrapeResult =
        withContext(Dispatchers.IO) {
            try {
                httpClient.newCall(createRequest(url, apiKey)).execute().use { response ->
                    val source = response.body?.source()
                    source?.request(MAX_RESPONSE_BYTES + 1)
                    if (source != null && source.buffer.size > MAX_RESPONSE_BYTES) {
                        return@withContext FirecrawlScrapeResult.Failure(
                            "The extracted page is too large for a preview", false
                        )
                    }
                    val responseBody = source?.buffer?.readUtf8().orEmpty()
                    if (!response.isSuccessful) {
                        return@withContext FirecrawlScrapeResult.Failure(
                            message = when (response.code) {
                                401, 403 -> "Firecrawl requires a personal API key on this network. Add or replace it in Settings."
                                402 -> "Firecrawl credits are unavailable. Check your Firecrawl account."
                                429 -> "Firecrawl's rate limit was reached. Refresh the preview later."
                                in 500..599 -> "Firecrawl is temporarily unavailable."
                                else -> responseBody.readErrorMessage()
                                    ?: "Firecrawl returned HTTP ${response.code}"
                            },
                            retryable = response.code == 408 || response.code == 425 ||
                                response.code == 429 || response.code >= 500
                        )
                    }
                    parseResponse(responseBody)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                FirecrawlScrapeResult.Failure(
                    message = "Firecrawl could not be reached. Check your connection.",
                    retryable = true
                )
            } catch (exception: Exception) {
                FirecrawlScrapeResult.Failure(
                    message = "Firecrawl returned an invalid response",
                    retryable = false
                )
            }
        }

    internal fun parseResponse(body: String): FirecrawlScrapeResult {
        return runCatching {
            val root = JSONObject(body)
            if (!root.optBoolean("success", false)) {
                return FirecrawlScrapeResult.Failure(
                    message = root.stringValue("error", "message") ?: "Firecrawl could not scrape this page",
                    retryable = false
                )
            }

            val data = root.optJSONObject("data")
                ?: return FirecrawlScrapeResult.Failure("Firecrawl returned no page data", false)
            val metadata = data.optJSONObject("metadata") ?: JSONObject()
            val markdown = data.stringValue("markdown")?.take(MAX_CONTENT_LENGTH)
            val pageStatus = metadata.optInt("statusCode", 200)
            if (pageStatus >= 400) {
                return FirecrawlScrapeResult.Failure(
                    message = "The page returned HTTP $pageStatus",
                    retryable = pageStatus == 408 || pageStatus == 425 ||
                        pageStatus == 429 || pageStatus >= 500
                )
            }

            val page = WebPageData(
                title = metadata.stringValue("ogTitle", "title")?.take(MAX_TITLE_LENGTH),
                description = metadata.stringValue("ogDescription", "description")
                    ?.take(MAX_DESCRIPTION_LENGTH),
                imageUrl = metadata.stringValue("ogImage"),
                markdown = markdown,
                canonicalUrl = metadata.stringValue("ogUrl", "sourceURL"),
                siteName = metadata.stringValue("ogSiteName")?.take(MAX_METADATA_LENGTH),
                author = metadata.stringValue("author", "article:author", "byline")
                    ?.take(MAX_METADATA_LENGTH),
                publishedAt = metadata.stringValue(
                    "publishedTime",
                    "article:published_time",
                    "datePublished",
                    "date"
                )?.take(MAX_METADATA_LENGTH),
                language = metadata.stringValue("language")?.take(MAX_LANGUAGE_LENGTH),
                readingTimeMinutes = markdown?.readingTimeMinutes()
            )

            if (page.hasUsefulData) {
                FirecrawlScrapeResult.Success(page)
            } else {
                FirecrawlScrapeResult.Failure("Firecrawl found no readable page content", false)
            }
        }.getOrElse { exception ->
            FirecrawlScrapeResult.Failure(
                message = exception.message ?: "Firecrawl returned malformed data",
                retryable = false
            )
        }
    }

    private fun String.readErrorMessage(): String? = runCatching {
        JSONObject(this).stringValue("error", "message")?.take(MAX_ERROR_LENGTH)
    }.getOrNull()

    private fun JSONObject.stringValue(vararg keys: String): String? {
        return keys.firstNotNullOfOrNull { key ->
            (opt(key) as? String)?.trim()?.takeIf(String::isNotBlank)
        }
    }

    private fun String.readingTimeMinutes(): Int {
        val words = split(Regex("\\s+")).count(String::isNotBlank)
        return ceil(words / WORDS_PER_MINUTE.toDouble()).toInt().coerceAtLeast(1)
    }

    private companion object {
        const val SCRAPE_ENDPOINT = "https://api.firecrawl.dev/v2/scrape"
        const val CACHE_MAX_AGE_MS = 86_400_000L
        const val MAX_RESPONSE_BYTES = 2_000_000L
        const val MAX_CONTENT_LENGTH = 40_000
        const val MAX_TITLE_LENGTH = 300
        const val MAX_DESCRIPTION_LENGTH = 1_500
        const val MAX_METADATA_LENGTH = 300
        const val MAX_LANGUAGE_LENGTH = 32
        const val MAX_ERROR_LENGTH = 300
        const val WORDS_PER_MINUTE = 225
        val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    }
}
