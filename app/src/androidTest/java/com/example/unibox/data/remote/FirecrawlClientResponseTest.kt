package com.example.unibox.data.remote

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.unibox.data.repository.WebPreviewPreferencesImpl
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirecrawlClientResponseTest {
    private val client = FirecrawlClient(OkHttpClient())

    @Test
    fun enhancedExtractionRequiresExplicitPersistentOptIn() = runBlocking {
        val context = isolatedPreferencesContext()
        context.deleteSharedPreferences("unibox_web_preview_prefs")
        try {
            val preferences = WebPreviewPreferencesImpl(context)
            assertFalse(preferences.isFirecrawlEnabled())
            preferences.setFirecrawlEnabled(true)
            assertTrue(WebPreviewPreferencesImpl(context).isFirecrawlEnabled())
            preferences.setFirecrawlEnabled(false)
            assertFalse(preferences.isFirecrawlEnabled())
        } finally {
            context.deleteSharedPreferences("unibox_web_preview_prefs")
        }
    }

    @Test
    fun personalKeyIsEncryptedAndCanBeRemoved() = runBlocking {
        val context = isolatedPreferencesContext()
        context.deleteSharedPreferences("unibox_web_preview_prefs")
        try {
            val preferences = WebPreviewPreferencesImpl(context)
            val testKey = "fc-unibox-test-key-not-a-real-credential"
            preferences.setFirecrawlApiKey(testKey)
            assertEquals(testKey, WebPreviewPreferencesImpl(context).getFirecrawlApiKey())
            val storedValues = context.getSharedPreferences("unibox_web_preview_prefs", 0).all
            assertFalse(storedValues.values.any { it.toString().contains(testKey) })
            preferences.setFirecrawlApiKey(null)
            assertNull(preferences.getFirecrawlApiKey())
        } finally {
            context.deleteSharedPreferences("unibox_web_preview_prefs")
        }
    }

    @Test
    fun requestsOnlyAttachOptionalServiceAuthentication() {
        val keyless = client.createRequest("https://example.com")
        assertNull(keyless.header("Authorization"))
        assertNull(keyless.header("Cookie"))
        val authenticated = client.createRequest("https://example.com", "fc-test-key")
        assertEquals("https://api.firecrawl.dev/v2/scrape", authenticated.url.toString())
        assertEquals("Bearer fc-test-key", authenticated.header("Authorization"))
        assertNull(authenticated.header("Cookie"))
    }

    @Test
    fun parsesDocumentAndOpenGraphMetadata() {
        val result = client.parseResponse(
            """{
                "success": true,
                "data": {
                    "markdown": "A useful page with enough readable words for search.",
                    "metadata": {
                        "title": "Fallback title",
                        "ogTitle": "Readable article",
                        "ogDescription": "A clean article description.",
                        "ogImage": "https://example.com/cover.jpg",
                        "ogUrl": "https://example.com/article",
                        "ogSiteName": "Example Journal",
                        "author": "Sam Writer",
                        "publishedTime": "2026-08-20T10:00:00Z",
                        "language": "en",
                        "statusCode": 200
                    }
                }
            }""".trimIndent()
        )

        assertTrue(result is FirecrawlScrapeResult.Success)
        val page = (result as FirecrawlScrapeResult.Success).page
        assertEquals("Readable article", page.title)
        assertEquals("A clean article description.", page.description)
        assertEquals("https://example.com/cover.jpg", page.imageUrl)
        assertEquals("https://example.com/article", page.canonicalUrl)
        assertEquals("Example Journal", page.siteName)
        assertEquals("Sam Writer", page.author)
        assertEquals("en", page.language)
        assertEquals(1, page.readingTimeMinutes)
    }

    @Test
    fun treatsRateLimitedPageResponseAsRetryable() {
        val result = client.parseResponse(
            """{
                "success": true,
                "data": {
                    "metadata": { "statusCode": 429 }
                }
            }""".trimIndent()
        )

        assertTrue(result is FirecrawlScrapeResult.Failure)
        assertTrue((result as FirecrawlScrapeResult.Failure).retryable)
    }

    @Test
    fun rejectsOversizedResponsesWithoutRetrying() = runBlocking {
        val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body("x".repeat(2_000_001).toResponseBody("application/json".toMediaType()))
                .build()
        }.build()

        val result = FirecrawlClient(httpClient).scrape("https://example.com")

        assertTrue(result is FirecrawlScrapeResult.Failure)
        assertFalse((result as FirecrawlScrapeResult.Failure).retryable)
        assertEquals("The extracted page is too large for a preview", result.message)
    }

    private fun isolatedPreferencesContext(): Context =
        object : ContextWrapper(InstrumentationRegistry.getInstrumentation().targetContext) {
            // Instrumentation runs as the app UID; isolate test files within its writable sandbox.
            override fun getSharedPreferences(name: String, mode: Int): SharedPreferences =
                super.getSharedPreferences("phase3_test_$name", mode)

            override fun deleteSharedPreferences(name: String): Boolean =
                super.deleteSharedPreferences("phase3_test_$name")
        }
}
