package com.example.unibox.presentation.main

import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.model.UniBoxItem

internal fun List<UniBoxItem>.applyWorkflow(
    section: LibrarySection,
    sortOrder: ItemSortOrder,
    favoriteOnly: Boolean,
    selectedCollection: String?,
    now: Long = System.currentTimeMillis()
): List<UniBoxItem> {
    return asSequence()
        .filter { item ->
            when (section) {
                LibrarySection.INBOX -> {
                    item.status == ItemStatus.INBOX &&
                        (item.snoozedUntil == null || item.snoozedUntil <= now)
                }
                LibrarySection.LIBRARY -> item.status == ItemStatus.SAVED
                LibrarySection.ARCHIVE -> item.status == ItemStatus.ARCHIVED
            }
        }
        .filter { !favoriteOnly || it.isFavorite }
        .filter { selectedCollection == null || it.collectionName == selectedCollection }
        .let { sequence ->
            when (sortOrder) {
                ItemSortOrder.NEWEST -> sequence.sortedByDescending(UniBoxItem::timestamp)
                ItemSortOrder.OLDEST -> sequence.sortedBy(UniBoxItem::timestamp)
                ItemSortOrder.TITLE -> sequence.sortedBy { it.title.lowercase() }
            }
        }
        .toList()
}
