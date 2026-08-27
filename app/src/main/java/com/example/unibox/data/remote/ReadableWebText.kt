package com.example.unibox.data.remote

/** A plain-text excerpt for search and native Text; never renders remote HTML. */
internal fun String.toReadableText(): String =
    replace(Regex("!\\[[^]]*]\\([^)]*\\)"), "")
        .replace(Regex("\\[([^]]+)]\\([^)]*\\)"), "$1")
        .replace(Regex("(?m)^\\s{0,3}#{1,6}\\s+"), "")
        .replace(Regex("(?m)^\\s*>\\s?"), "")
        .replace(Regex("(?m)^\\s*```[^\\n]*\\n?"), "")
        .replace(Regex("<[^>]+>"), "")
        .replace("**", "")
        .replace("__", "")
        .replace("`", "")
        .replace(Regex("\\n{3,}"), "\n\n")
        .trim()
