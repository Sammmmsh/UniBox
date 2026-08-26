package com.example.unibox.presentation.main

import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.model.UniBoxItem
import org.junit.Assert.assertEquals
import org.junit.Test

class LibraryWorkflowTest {

    @Test
    fun inbox_excludesProcessedArchivedAndFutureSnoozedItems() {
        val now = 10_000L
        val items = listOf(
            item(1, "Ready", ItemStatus.INBOX),
            item(2, "Due", ItemStatus.INBOX, snoozedUntil = now),
            item(3, "Later", ItemStatus.INBOX, snoozedUntil = now + 1),
            item(4, "Kept", ItemStatus.SAVED),
            item(5, "Archived", ItemStatus.ARCHIVED)
        )

        val result = items.applyWorkflow(
            section = LibrarySection.INBOX,
            sortOrder = ItemSortOrder.NEWEST,
            favoriteOnly = false,
            selectedCollection = null,
            now = now
        )

        assertEquals(listOf(2L, 1L), result.map(UniBoxItem::id))
    }

    @Test
    fun eachSectionContainsOnlyItsWorkflowState() {
        val items = listOf(
            item(1, "Inbox", ItemStatus.INBOX),
            item(2, "Library", ItemStatus.SAVED),
            item(3, "Archive", ItemStatus.ARCHIVED)
        )

        assertEquals(
            listOf(2L),
            items.applyWorkflow(
                LibrarySection.LIBRARY,
                ItemSortOrder.NEWEST,
                favoriteOnly = false,
                selectedCollection = null
            ).map(UniBoxItem::id)
        )
        assertEquals(
            listOf(3L),
            items.applyWorkflow(
                LibrarySection.ARCHIVE,
                ItemSortOrder.NEWEST,
                favoriteOnly = false,
                selectedCollection = null
            ).map(UniBoxItem::id)
        )
    }

    @Test
    fun favoriteCollectionAndTitleFiltersCompose() {
        val items = listOf(
            item(1, "Zulu", ItemStatus.SAVED, favorite = true, collection = "Work"),
            item(2, "Alpha", ItemStatus.SAVED, favorite = true, collection = "Work"),
            item(3, "Beta", ItemStatus.SAVED, favorite = false, collection = "Work"),
            item(4, "Gamma", ItemStatus.SAVED, favorite = true, collection = "Home")
        )

        val result = items.applyWorkflow(
            section = LibrarySection.LIBRARY,
            sortOrder = ItemSortOrder.TITLE,
            favoriteOnly = true,
            selectedCollection = "Work"
        )

        assertEquals(listOf("Alpha", "Zulu"), result.map(UniBoxItem::title))
    }

    private fun item(
        id: Long,
        title: String,
        status: ItemStatus,
        snoozedUntil: Long? = null,
        favorite: Boolean = false,
        collection: String? = null
    ) = UniBoxItem(
        id = id,
        title = title,
        status = status,
        snoozedUntil = snoozedUntil,
        isFavorite = favorite,
        collectionName = collection,
        timestamp = id
    )
}
