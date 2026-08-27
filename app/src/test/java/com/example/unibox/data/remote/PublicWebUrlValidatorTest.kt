package com.example.unibox.data.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PublicWebUrlValidatorTest {
    private val validator = PublicWebUrlValidator()

    @Test
    fun structuralValidationRejectsPrivateAndCredentialedUrls() {
        assertFalse(validator.isStructurallyPublic("http://localhost/private"))
        assertFalse(validator.isStructurallyPublic("http://192.168.1.4/dashboard"))
        assertFalse(validator.isStructurallyPublic("https://user:secret@example.com"))
        assertFalse(validator.isStructurallyPublic("file:///tmp/private.txt"))
        assertTrue(validator.isStructurallyPublic("https://example.com/article"))
    }

    @Test
    fun sanitizerRemovesTrackingAndFragmentsButKeepsContentParameters() {
        val sanitized = validator.sanitizeTrackingParameters(
            "https://example.com/article?utm_source=newsletter&id=42&fbclid=abc#comments"
        )

        assertEquals("https://example.com/article?id=42", sanitized)
    }
}
