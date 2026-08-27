package com.example.unibox.data.remote

import com.example.unibox.domain.model.WebEnrichmentStatus

data class WebPageData(
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val markdown: String? = null,
    val canonicalUrl: String? = null,
    val siteName: String? = null,
    val author: String? = null,
    val publishedAt: String? = null,
    val language: String? = null,
    val readingTimeMinutes: Int? = null
) {
    val hasUsefulData: Boolean
        get() = listOf(title, description, imageUrl, markdown).any { !it.isNullOrBlank() }
}

sealed interface FirecrawlScrapeResult {
    data class Success(val page: WebPageData) : FirecrawlScrapeResult

    data class Failure(
        val message: String,
        val retryable: Boolean
    ) : FirecrawlScrapeResult
}

sealed interface WebEnrichmentResult {
    data class Success(
        val page: WebPageData,
        val provider: String,
        val complete: Boolean,
        val shouldRetry: Boolean = false,
        val warning: String? = null
    ) : WebEnrichmentResult

    data class Failure(
        val message: String,
        val retryable: Boolean
    ) : WebEnrichmentResult
}

object WebEnrichmentProviders {
    const val FIRECRAWL = "Firecrawl"
    const val DIRECT = "Direct"
}

internal data class EnrichmentDecision(
    val status: WebEnrichmentStatus,
    val retry: Boolean
)

internal fun WebEnrichmentResult.decision(attempt: Int): EnrichmentDecision {
    val canRetry = when (this) {
        is WebEnrichmentResult.Success -> shouldRetry
        is WebEnrichmentResult.Failure -> retryable
    }
    val retry = canRetry && attempt < 2
    val status = when {
        retry -> WebEnrichmentStatus.PENDING
        this is WebEnrichmentResult.Success && complete -> WebEnrichmentStatus.COMPLETE
        this is WebEnrichmentResult.Success -> WebEnrichmentStatus.PARTIAL
        else -> WebEnrichmentStatus.FAILED
    }
    return EnrichmentDecision(status, retry)
}
