package com.example.unibox.domain.model

import androidx.compose.ui.graphics.Color

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
    val imageUri: String? = null
)

/**
 * Content categories for auto-tagging saved items.
 * Each category has a display label, icon name, and brand color.
 */
enum class Category(
    val displayName: String,
    val emoji: String,
    val tint: Long
) {
    FOOD("Food", "🍕", 0xFFF97316),
    TECH("Tech", "💻", 0xFF6366F1),
    ARTICLE("Article", "📰", 0xFF0EA5E9),
    VIDEO("Video", "🎬", 0xFFEF4444),
    SOCIAL("Social", "💬", 0xFF8B5CF6),
    SHOPPING("Shopping", "🛍️", 0xFFEC4899),
    TRAVEL("Travel", "✈️", 0xFF14B8A6),
    MUSIC("Music", "🎵", 0xFF22C55E),
    RECIPE("Recipe", "👨‍🍳", 0xFFF59E0B),
    BOOKMARK("Bookmark", "🔖", 0xFF64748B),
    UNCATEGORIZED("Inbox", "📥", 0xFF94A3B8);

    val color: Color get() = Color(tint)
}
