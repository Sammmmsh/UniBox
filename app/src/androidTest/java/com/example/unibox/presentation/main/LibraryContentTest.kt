package com.example.unibox.presentation.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.presentation.components.LibrarySearchBar
import com.example.unibox.presentation.components.UniBoxCard
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class LibraryContentTest {
    @get:Rule val compose = createComposeRule()

    @Test fun collectionShortcutAndVisibleCardMetadata() {
        var selected: String? = null
        val item = UniBoxItem(id = 1, title = "Kotlin layouts", category = Category.TECH,
            status = ItemStatus.SAVED, collectionName = "Work", tags = listOf("Android", "Design"))
        compose.setContent {
            MaterialTheme {
                LibraryContent(
                    MainUiState(items = listOf(item), isLoading = false, section = LibrarySection.LIBRARY,
                        availableCategories = listOf(Category.TECH),
                        collectionSummaries = listOf(CollectionSummary("Work", 1))),
                    onCollection = { selected = it }
                )
            }
        }
        compose.onNodeWithText("Work · 1").performClick()
        compose.runOnIdle { assertEquals("Work", selected) }
        compose.onNodeWithTag("library-grid").performScrollToNode(hasText("Kotlin layouts"))
        compose.onNodeWithText("Android  ·  Design").assertExists()
        compose.onNodeWithText("Work").assertExists()
    }

    @Test fun switchingSectionsStartsAtTheFirstResultInsteadOfTheOldScrollOffset() {
        val counts = mapOf(LibrarySection.INBOX to 40, LibrarySection.LIBRARY to 40)
        var state by mutableStateOf(MainUiState(items = items("Inbox"), isLoading = false, sectionCounts = counts))
        compose.setContent {
            MaterialTheme {
                LibraryContent(state, onSection = {
                    state = state.copy(section = it, items = items("Saved"))
                })
            }
        }
        compose.onNodeWithTag("library-grid").performScrollToIndex(30)
        compose.onNodeWithText("Library · 40").performClick()
        compose.onNodeWithContentDescription("Search saved items").assertIsDisplayed()
        compose.onNodeWithText("Saved 0").assertIsDisplayed()
    }

    @Test fun filteredEmptyStateCanBeClearedInDarkThemeWithLargeText() {
        var cleared = false
        compose.setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, 1.6f)) {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    LibraryContent(
                        MainUiState(isLoading = false, searchQuery = "No match", favoriteOnly = true),
                        onClear = { cleared = true }
                    )
                }
            }
        }
        compose.onNodeWithTag("library-grid").performScrollToNode(hasText("Clear search and filters"))
        compose.onNodeWithText("Clear search and filters").performClick()
        compose.runOnIdle { assertTrue(cleared) }
    }

    @Test fun searchHasAVisibleHintAcceptsInputAndClears() {
        var query by mutableStateOf("")
        compose.setContent {
            MaterialTheme { LibrarySearchBar(query, { query = it }, placeholder = "Search this inbox") }
        }
        compose.onNodeWithText("Search this inbox").assertIsDisplayed()
        compose.onNodeWithContentDescription("Search saved items").performTextInput("Kotlin")
        compose.onNodeWithContentDescription("Clear search").performClick()
        compose.runOnIdle { assertEquals("", query) }
        compose.onNodeWithText("Search this inbox").assertIsDisplayed()
    }

    @Test fun favoriteTouchTargetDoesNotOpenTheCard() {
        var opened = false
        var starred = false
        compose.setContent {
            MaterialTheme {
                UniBoxCard(UniBoxItem(title = "A note"), onClick = { opened = true }, onToggleFavorite = { starred = true })
            }
        }
        compose.onNodeWithContentDescription("Add to favorites")
            .assertHeightIsAtLeast(48.dp).performClick()
        compose.runOnIdle {
            assertTrue(starred)
            assertFalse(opened)
        }
    }

    private fun items(prefix: String) = (0..39).map {
        UniBoxItem(id = it.toLong() + 1, title = prefix + " " + it,
            description = "A saved idea with enough detail to occupy a card.", category = Category.TECH)
    }
}
