package com.example.unibox.data.remote

import com.example.unibox.domain.repository.WebPreviewPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebContentEnricher @Inject constructor(
    private val firecrawlClient: FirecrawlClient,
    private val openGraphParser: OpenGraphParser,
    private val urlValidator: PublicWebUrlValidator,
    private val preferences: WebPreviewPreferences
) {
    suspend fun enrich(url: String): WebEnrichmentResult {
        val safeUrl = try {
            urlValidator.sanitizeIfPublic(url)
        } catch (exception: IOException) {
            return WebEnrichmentResult.Failure("The page could not be reached. UniBox will retry.", true)
        }
            ?: return WebEnrichmentResult.Failure(
                message = "Only public HTTP and HTTPS pages can be previewed",
                retryable = false
            )

        if (!preferences.isFirecrawlEnabled()) {
            return directPreview(safeUrl)
        }

        return when (val firecrawl = firecrawlClient.scrape(safeUrl, preferences.getFirecrawlApiKey())) {
            is FirecrawlScrapeResult.Success -> WebEnrichmentResult.Success(
                page = sanitizePage(firecrawl.page, safeUrl),
                provider = WebEnrichmentProviders.FIRECRAWL,
                complete = !firecrawl.page.markdown.isNullOrBlank()
            )

            is FirecrawlScrapeResult.Failure -> {
                val fallback = directPage(safeUrl)
                if (fallback.hasUsefulData) {
                    WebEnrichmentResult.Success(
                        page = fallback,
                        provider = WebEnrichmentProviders.DIRECT,
                        complete = false,
                        shouldRetry = firecrawl.retryable,
                        warning = firecrawl.message
                    )
                } else {
                    WebEnrichmentResult.Failure(firecrawl.message, firecrawl.retryable)
                }
            }
        }
    }

    private suspend fun directPreview(url: String): WebEnrichmentResult {
        val page = directPage(url)
        return if (page.hasUsefulData) {
            WebEnrichmentResult.Success(
                page = page,
                provider = WebEnrichmentProviders.DIRECT,
                complete = false
            )
        } else {
            WebEnrichmentResult.Failure(
                message = "This page did not expose preview metadata",
                retryable = true
            )
        }
    }

    private suspend fun directPage(url: String): WebPageData = withContext(Dispatchers.IO) {
        sanitizePage(openGraphParser.parse(url).toWebPageData(), url)
    }

    private suspend fun sanitizePage(page: WebPageData, sourceUrl: String): WebPageData {
        suspend fun safeReference(reference: String?): String? {
            val resolved = reference?.let { sourceUrl.toHttpUrlOrNull()?.resolve(it) }
                ?: return null
            return try {
                urlValidator.sanitizeIfPublic(resolved.toString())
            } catch (exception: IOException) {
                null
            }
        }
        return page.copy(
            imageUrl = safeReference(page.imageUrl),
            canonicalUrl = safeReference(page.canonicalUrl)
        )
    }
}
