package com.example.unibox.domain.organization

import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OrganizationEngineTest {
    private val engine = OrganizationEngine()

    @Test
    fun partialWordsDoNotBecomeCategorySignals() {
        assertNull(engine.classify(UniBoxItem(title = "Capital workshop tripod")))
    }

    @Test
    fun domainsMustMatchTheActualHost() {
        assertEquals(Category.VIDEO, engine.classify(link("https://m.youtube.com/watch?v=123"))?.value)
        assertNull(engine.classify(link("https://notyoutube.com/video")))
        assertNull(engine.classify(link("https://example.com/?source=youtube.com")))
        assertNull(engine.classify(link("https://youtube.com.example.org")))
    }

    @Test
    fun generalPublishingSitesAreNotAutomaticallyTechnology() {
        assertNull(engine.classify(link("https://medium.com/a-story")))
        assertEquals(
            Category.RECIPE,
            engine.classify(
                UniBoxItem(title = "Sourdough recipe and ingredients", url = "https://medium.com/bread")
            )?.value
        )
    }

    @Test
    fun ambiguousSubjectsAreLeftUnsorted() {
        assertNull(engine.classify(UniBoxItem(title = "Android travel")))
    }

    @Test
    fun meaningfulExtractedContentCanSuggestAnUnsortedCategory() {
        val item = link("https://example.com/article").copy(
            extractedText = "Kotlin Android programming software gradle"
        )
        assertEquals(Category.TECH, engine.suggest(item, emptyList()).category?.value)
    }

    @Test
    fun suggestionsRespectExistingCategoryAndCollection() {
        val item = UniBoxItem(
            id = 1, title = "Kotlin Android layouts",
            category = Category.BOOKMARK, collectionName = "My research"
        )
        val suggestion = engine.suggest(item, listOf(item))
        assertNull(suggestion.category)
        assertNull(suggestion.collection)
    }

    @Test
    fun existingTagsAreNotRepeatedAndSuggestionsAreBounded() {
        val item = UniBoxItem(
            title = "Kotlin Android design accessibility photography",
            tags = listOf("kOtLiN")
        )
        val tags = engine.suggest(item, emptyList()).tags.map { it.value }
        assertFalse(tags.contains("Kotlin"))
        assertTrue(tags.size <= 4)
        assertTrue(tags.contains("Android"))
    }

    @Test
    fun collectionSuggestionUsesSpecificSharedContent() {
        val current = UniBoxItem(id = 1, title = "Kotlin Android layouts")
        val saved = UniBoxItem(
            id = 2, title = "Kotlin Android navigation", collectionName = "App research"
        )
        val suggestion = engine.suggest(current, listOf(current, saved)).collection
        assertEquals("App research", suggestion?.value)
        assertTrue(suggestion?.reason?.contains(saved.title) == true)
    }

    @Test
    fun genericWordsAndTheSameWebsiteDoNotPickACollection() {
        val current = link("https://example.com/one").copy(id = 1, title = "The best guide")
        val saved = link("https://example.com/two").copy(
            id = 2, title = "The best tips", collectionName = "Research"
        )
        assertNull(engine.suggest(current, listOf(saved)).collection)
    }

    @Test
    fun equallyStrongCollectionsDoNotProduceAnArbitraryChoice() {
        val current = UniBoxItem(id = 1, title = "Kotlin Android layouts")
        val saved = UniBoxItem(id = 2, title = "Kotlin Android", collectionName = "Work")
        assertNull(engine.suggest(current, listOf(saved, saved.copy(id = 3, collectionName = "Personal"))).collection)
    }

    private fun link(url: String) = UniBoxItem(title = url, url = url)
}
