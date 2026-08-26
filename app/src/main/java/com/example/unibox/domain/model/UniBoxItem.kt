package com.example.unibox.domain.model

/**
 * Core domain model representing a saved item in the UniBox inbox.
 * This is framework-agnostic — no Room annotations here (those live in the data layer).
 */
data class UniBoxItem(
    val id: Long = 0L,
    val title: String,
    val description: String = "",
    val url: String? = null,
    val thumbnailUrl: String? = null,
    val extractedText: String? = null,
    val category: Category = Category.UNCATEGORIZED,
    val sourceApp: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationLabel: String? = null,
    val imageUri: String? = null,
    val imageUris: List<String> = emptyList()
)

/**
 * Content categories for auto-tagging saved items.
 * Visual treatment belongs to the presentation layer so categories remain
 * readable and neutral across themes.
 */
enum class Category(
    val displayName: String
) {
    FOOD("Food"),
    TECH("Technology"),
    ARTICLE("Article"),
    VIDEO("Video"),
    SOCIAL("Social"),
    SHOPPING("Shopping"),
    TRAVEL("Travel"),
    MUSIC("Music"),
    RECIPE("Recipe"),
    BOOKMARK("Bookmark"),
    UNCATEGORIZED("Unsorted")
}
