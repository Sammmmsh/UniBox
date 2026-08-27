package com.example.unibox.data.local

import org.junit.Assert.*
import org.junit.Test

class LibrarySearchQueryTest {
    @Test fun quotesAndPunctuationBecomeLiteralPrefixTerms() {
        assertEquals("\"android*\" \"kotlin*\"", librarySearchQuery("\"Android\" (Kotlin):"))
    }

    @Test fun searchOperatorsAreQuotedRatherThanExecuted() {
        assertEquals("\"or*\" \"near*\" \"not*\"", librarySearchQuery("OR NEAR NOT"))
    }

    @Test fun punctuationOnlyDoesNotReachMatch() {
        assertNull(librarySearchQuery("\"()*+-:"))
        assertNull(librarySearchQuery("   "))
    }

    @Test fun everyWordGetsPrefixMatchingAndDuplicatesAreRemoved() {
        assertEquals("\"andr*\" \"lay*\"", librarySearchQuery("andr lay ANDR"))
    }

    @Test fun unicodeWordsAreKeptAndLargeQueriesAreBounded() {
        assertEquals("\"café*\" \"東京*\"", librarySearchQuery("Café 東京"))
        assertEquals(20, librarySearchQuery((1..100).joinToString(" "))!!.split(" ").size)
    }
}
