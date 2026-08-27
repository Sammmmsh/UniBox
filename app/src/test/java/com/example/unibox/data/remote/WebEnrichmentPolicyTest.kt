package com.example.unibox.data.remote

import com.example.unibox.domain.model.WebEnrichmentStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.InetAddress

class WebEnrichmentPolicyTest {
    @Test
    fun transientFailuresStopRetryingAfterThreeAttempts() {
        val failure = WebEnrichmentResult.Failure("Temporary outage", retryable = true)
        assertTrue(failure.decision(0).retry)
        assertTrue(failure.decision(1).retry)
        assertFalse(failure.decision(2).retry)
        assertEquals(WebEnrichmentStatus.FAILED, failure.decision(2).status)
    }

    @Test
    fun successfulFallbackRemainsAvailableAfterRetryLimit() {
        val fallback = WebEnrichmentResult.Success(
            page = WebPageData(title = "Basic preview"),
            provider = WebEnrichmentProviders.DIRECT,
            complete = false,
            shouldRetry = true
        )
        assertEquals(WebEnrichmentStatus.PENDING, fallback.decision(0).status)
        assertEquals(WebEnrichmentStatus.PARTIAL, fallback.decision(2).status)
        assertFalse(fallback.decision(2).retry)
    }

    @Test
    fun rejectsNonPublicIpv6AndSharedAddressRanges() {
        assertFalse(InetAddress.getByName("fc00::1").isPublicWebAddress())
        assertFalse(InetAddress.getByName("fe80::1").isPublicWebAddress())
        assertFalse(InetAddress.getByName("100.64.0.1").isPublicWebAddress())
        assertFalse(InetAddress.getByName("127.0.0.1").isPublicWebAddress())
        assertTrue(InetAddress.getByName("8.8.8.8").isPublicWebAddress())
    }

    @Test
    fun pageExcerptIsReadableWithoutMarkdownSyntax() {
        assertEquals(
            "An article\n\nRead this useful guide.",
            "# An article\n\nRead **this** [useful guide](https://example.com).".toReadableText()
        )
    }
}
