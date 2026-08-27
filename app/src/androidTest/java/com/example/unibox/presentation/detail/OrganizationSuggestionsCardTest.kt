package com.example.unibox.presentation.detail

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.organization.OrganizationHint
import com.example.unibox.domain.organization.OrganizationSelection
import com.example.unibox.domain.organization.OrganizationSuggestions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class OrganizationSuggestionsCardTest {
    @get:Rule
    val compose = createComposeRule()

    private val suggestions = OrganizationSuggestions(
        category = OrganizationHint(Category.TECH, "Matches kotlin, android"),
        tags = listOf(
            OrganizationHint("Kotlin", "Matches kotlin"),
            OrganizationHint("Android", "Matches android")
        ),
        collection = OrganizationHint("App research", "Similar to an existing saved item")
    )

    @Test
    fun onlySelectedSuggestionsAreApplied() {
        var applied: OrganizationSelection? = null
        compose.setContent {
            MaterialTheme {
                OrganizationSuggestionsCard(suggestions, false, { applied = it }, {})
            }
        }
        compose.onNodeWithText("Android").performClick()
        compose.onNodeWithText("Apply selected").performClick()
        compose.runOnIdle {
            assertEquals(listOf("Kotlin"), applied?.tags)
            assertEquals(Category.TECH, applied?.category)
            assertEquals("App research", applied?.collectionName)
        }
    }

    @Test
    fun dismissDoesNotApplyChangesInDarkTheme() {
        var applied: OrganizationSelection? = null
        var dismissed = false
        compose.setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                OrganizationSuggestionsCard(suggestions, false, { applied = it }, { dismissed = true })
            }
        }
        compose.onNodeWithText("Dismiss").performClick()
        compose.runOnIdle {
            assertTrue(dismissed)
            assertNull(applied)
        }
    }

    @Test
    fun emptySelectionCannotBeApplied() {
        compose.setContent {
            MaterialTheme {
                OrganizationSuggestionsCard(suggestions, false, {}, {})
            }
        }
        listOf("Technology", "Kotlin", "Android", "App research").forEach {
            compose.onNodeWithText(it).performClick()
        }
        compose.onNodeWithText("Apply selected").assertIsNotEnabled()
    }
}
