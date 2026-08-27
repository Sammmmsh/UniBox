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
    val imageUris: List<String> = emptyList(),
    val status: ItemStatus = ItemStatus.INBOX,
    val isFavorite: Boolean = false,
    val snoozedUntil: Long? = null,
    val userNote: String = "",
    val collectionName: String? = null,
    val tags: List<String> = emptyList(),
    val enrichmentStatus: WebEnrichmentStatus = WebEnrichmentStatus.NOT_REQUIRED,
    val enrichmentProvider: String? = null,
    val enrichmentError: String? = null,
    val canonicalUrl: String? = null,
    val webSiteName: String? = null,
    val webAuthor: String? = null,
    val webPublishedAt: String? = null,
    val webLanguage: String? = null,
    val webReadingTimeMinutes: Int? = null,
    val lastEnrichedAt: Long? = null,
    val updatedAt: Long = System.currentTimeMillis()
)

enum class ItemStatus {
    INBOX,
    SAVED,
    ARCHIVED
}

enum class WebEnrichmentStatus {
    NOT_REQUIRED,
    PENDING,
    COMPLETE,
    PARTIAL,
    FAILED
}

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
