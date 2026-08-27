package com.example.unibox.presentation.share

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ShareReceiverScreenTest {
    @get:Rule val compose = createComposeRule()
    private val data = SharedData(type = SharedDataType.TEXT, rawText = "An idea worth keeping")

    @Test fun saveAndCancelAreDisabledWhileSaving() {
        compose.setContent {
            MaterialTheme {
                ShareReceiverScreen(data, {}, {}, saveState = ShareSaveState(isSaving = true))
            }
        }
        compose.onNodeWithTag("share-content").performScrollToNode(hasText("Saving to inbox..."))
        compose.onNodeWithText("Saving to inbox...").assertIsNotEnabled()
        compose.onNodeWithText("Cancel").assertIsNotEnabled()
    }

    @Test fun errorAndRetryStayAccessibleInDarkThemeWithLargeText() {
        var retried = false
        compose.setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, 1.6f)) {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    ShareReceiverScreen(data, { retried = true }, {},
                        saveState = ShareSaveState(error = "Could not save. Your content is still here."))
                }
            }
        }
        compose.onNodeWithTag("share-content").performScrollToNode(hasText("Could not save. Your content is still here."))
        compose.onNodeWithText("Could not save. Your content is still here.").assertIsDisplayed()
        compose.onNodeWithTag("share-content").performScrollToNode(hasText("Try again"))
        compose.onNodeWithText("Try again").performClick()
        compose.runOnIdle { assertTrue(retried) }
    }

    @Test fun unsupportedContentCannotBeSaved() {
        compose.setContent { MaterialTheme { ShareReceiverScreen(SharedData(), {}, {}) } }
        compose.onNodeWithTag("share-content").performScrollToNode(hasText("Save to inbox"))
        compose.onNodeWithText("Save to inbox").assertIsNotEnabled()
    }
}
