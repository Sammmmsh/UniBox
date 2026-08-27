package com.example.unibox.presentation.main

import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.model.Category
import java.util.Locale

data class CollectionSummary(val name: String, val count: Int)

internal fun List<UniBoxItem>.inSection(
    section: LibrarySection,
    now: Long = System.currentTimeMillis()
): List<UniBoxItem> = filter { item ->
    when (section) {
        LibrarySection.INBOX -> item.status == ItemStatus.INBOX &&
            (item.snoozedUntil == null || item.snoozedUntil <= now)
        LibrarySection.LIBRARY -> item.status == ItemStatus.SAVED
        LibrarySection.ARCHIVE -> item.status == ItemStatus.ARCHIVED
    }
}

internal fun List<UniBoxItem>.collectionSummaries(): List<CollectionSummary> =
    mapNotNull { it.collectionName?.takeIf(String::isNotBlank) }
        .groupingBy { it }.eachCount()
        .map { (name, count) -> CollectionSummary(name, count) }
        .sortedBy { it.name.lowercase(Locale.ROOT) }

internal fun List<UniBoxItem>.availableCategories(selected: Category?): List<Category> =
    (map { it.category } + listOfNotNull(selected)).distinct().sortedBy { it.ordinal }

internal fun List<UniBoxItem>.applyWorkflow(
    section: LibrarySection,
    sortOrder: ItemSortOrder,
    favoriteOnly: Boolean,
    selectedCollection: String?,
    now: Long = System.currentTimeMillis()
): List<UniBoxItem> {
    return inSection(section, now).asSequence()
        .filter { !favoriteOnly || it.isFavorite }
        .filter { selectedCollection == null || it.collectionName == selectedCollection }
        .let { sequence ->
            when (sortOrder) {
                ItemSortOrder.NEWEST -> sequence.sortedByDescending(UniBoxItem::timestamp)
                ItemSortOrder.OLDEST -> sequence.sortedBy(UniBoxItem::timestamp)
                ItemSortOrder.TITLE -> sequence.sortedBy { it.title.lowercase(Locale.ROOT) }
            }
        }
        .toList()
}
