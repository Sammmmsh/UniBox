package com.example.unibox.data.local

import java.util.Locale

/** Treat the search box as words, never as a user-authored FTS expression. */
internal fun librarySearchQuery(input: String): String? {
    val words = Regex("[\\p{L}\\p{N}]+")
        .findAll(input.take(2_000))
        .map { it.value.lowercase(Locale.ROOT) }
        .distinct()
        .take(20)
        .toList()
    // In FTS4 the prefix marker belongs inside the quoted phrase.
    // https://www.sqlite.org/fts3.html#full_text_index_queries
    return words.takeIf { it.isNotEmpty() }?.joinToString(" ") { "\"" + it + "*\"" }
}
