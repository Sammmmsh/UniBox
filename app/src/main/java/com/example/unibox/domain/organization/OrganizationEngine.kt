package com.example.unibox.domain.organization

import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
import java.net.URI
import java.util.Locale
import javax.inject.Inject

/** Deterministic, on-device suggestions. No network calls or automatic library changes. */
class OrganizationEngine @Inject constructor() {
    fun classify(item: UniBoxItem): OrganizationHint<Category>? {
        val host = hostOf(item.url)
        PLATFORM_CATEGORIES.entries.firstOrNull { (domain, _) ->
            host == domain || host.endsWith("." + domain)
        }?.let { (domain, category) ->
            return OrganizationHint(category, "From " + domain)
        }

        val signals = signalsFor(item)
        val ranked = CATEGORY_TERMS.map { (category, terms) ->
            val matches = terms.filter { signals.score(it) > 0 }
            Triple(category, matches.sumOf(signals::score), matches)
        }.sortedByDescending { it.second }
        val best = ranked.first()
        val runnerUp = ranked.getOrNull(1)?.second ?: 0
        if (best.second >= 4 && best.second - runnerUp >= 2) {
            return OrganizationHint(best.first, "Matches " + best.third.take(3).joinToString(", "))
        }
        // A general publishing host alone is not evidence of a technology article.
        if (best.second == 0 && item.url != null && words(item.extractedText.orEmpty()).size >= 120) {
            return OrganizationHint(Category.ARTICLE, "Readable long-form page content")
        }
        return null
    }

    fun suggest(item: UniBoxItem, library: List<UniBoxItem>): OrganizationSuggestions {
        val signals = signalsFor(item)
        val tags = TAG_TERMS.mapNotNull { (tag, terms) ->
            val matched = terms.filter { signals.score(it) > 0 }
            val score = matched.sumOf(signals::score)
            if (score < 2 || item.tags.any { it.equals(tag, ignoreCase = true) }) null
            else score to OrganizationHint(tag, "Matches " + matched.take(2).joinToString(", "))
        }.sortedByDescending { it.first }.take(4).map { it.second }

        return OrganizationSuggestions(
            category = if (item.category == Category.UNCATEGORIZED) classify(item) else null,
            tags = tags,
            collection = if (item.collectionName.isNullOrBlank()) suggestCollection(item, library) else null
        )
    }

    private fun suggestCollection(item: UniBoxItem, library: List<UniBoxItem>): OrganizationHint<String>? {
        val itemTerms = topicWords(item)
        if (itemTerms.isEmpty()) return null
        val matches = library.asSequence()
            .filter { it.id != item.id && !it.collectionName.isNullOrBlank() }
            .mapNotNull { saved ->
                val overlap = itemTerms.intersect(topicWords(saved))
                // A shared website or broad category alone should never pick a collection.
                if (overlap.size < 2) null
                else CollectionMatch(
                    name = requireNotNull(saved.collectionName),
                    score = overlap.size,
                    evidence = saved.title.take(70)
                )
            }
            .groupBy { it.name.lowercase(Locale.ROOT) }
            .map { (_, group) -> group.maxBy { it.score } }
            .sortedWith(compareByDescending<CollectionMatch> { it.score }.thenBy { it.name })
        val best = matches.firstOrNull() ?: return null
        if (matches.getOrNull(1)?.score == best.score) return null
        return OrganizationHint(best.name, "Similar to “" + best.evidence + "” in this collection")
    }

    private fun signalsFor(item: UniBoxItem): Signals = Signals(
        headline = words(item.title).toSet(),
        summary = words(item.description + " " + item.userNote).toSet(),
        body = words(item.extractedText.orEmpty().take(20_000)).toSet()
    )

    private fun topicWords(item: UniBoxItem): Set<String> =
        words(item.title + " " + item.description.take(500) + " " + item.tags.joinToString(" "))
            .filter { it.length > 2 && it !in STOP_WORDS }
            .toSet()

    private fun words(text: String): List<String> = WORD.findAll(
        WEB_URL.replace(text.take(40_000), " ").lowercase(Locale.ROOT)
    ).map { it.value }.toList()

    private fun hostOf(url: String?): String = runCatching {
        val uri = URI(url?.trim().orEmpty())
        if (uri.scheme?.lowercase(Locale.ROOT) !in setOf("http", "https")) ""
        else uri.host.orEmpty().lowercase(Locale.ROOT).trimEnd('.')
    }.getOrDefault("")

    private data class Signals(
        val headline: Set<String>,
        val summary: Set<String>,
        val body: Set<String>
    ) {
        fun score(term: String): Int =
            (if (term in headline) 4 else 0) +
                (if (term in summary) 2 else 0) +
                (if (term in body) 1 else 0)
    }

    private data class CollectionMatch(val name: String, val score: Int, val evidence: String)

    private companion object {
        val WEB_URL = Regex("https?://\\S+", RegexOption.IGNORE_CASE)
        val WORD = Regex("[\\p{L}\\p{N}]+")
        val STOP_WORDS = setOf(
            "the", "and", "for", "with", "this", "that", "from", "your", "you", "are", "was",
            "how", "why", "what", "when", "where", "into", "about", "have", "has", "not", "all",
            "our", "their", "its", "can", "will", "use", "using", "new", "best", "more", "some",
            "guide", "tips", "learn", "introduction", "getting", "started", "saved", "article",
            "video", "untitled", "shared", "content", "com", "www", "https", "http",
            "demo", "sample", "example", "note", "notes", "reference", "notebook"
        )
        val PLATFORM_CATEGORIES = linkedMapOf(
            "youtube.com" to Category.VIDEO, "youtu.be" to Category.VIDEO,
            "vimeo.com" to Category.VIDEO, "tiktok.com" to Category.VIDEO,
            "spotify.com" to Category.MUSIC, "soundcloud.com" to Category.MUSIC,
            "music.apple.com" to Category.MUSIC,
            "amazon.com" to Category.SHOPPING, "amazon.in" to Category.SHOPPING,
            "ebay.com" to Category.SHOPPING, "etsy.com" to Category.SHOPPING,
            "yelp.com" to Category.FOOD, "doordash.com" to Category.FOOD, "ubereats.com" to Category.FOOD,
            "booking.com" to Category.TRAVEL, "airbnb.com" to Category.TRAVEL,
            "tripadvisor.com" to Category.TRAVEL, "github.com" to Category.TECH,
            "stackoverflow.com" to Category.TECH, "developer.android.com" to Category.TECH,
            "dev.to" to Category.TECH, "twitter.com" to Category.SOCIAL, "x.com" to Category.SOCIAL,
            "instagram.com" to Category.SOCIAL, "reddit.com" to Category.SOCIAL,
            "allrecipes.com" to Category.RECIPE
        )
        val CATEGORY_TERMS = linkedMapOf(
            Category.RECIPE to listOf("recipe", "recipes", "ingredients", "preheat", "tablespoons", "tbsp", "bake"),
            Category.FOOD to listOf("restaurant", "restaurants", "cafe", "brunch", "dining", "sushi"),
            Category.TECH to listOf("kotlin", "android", "programming", "software", "gradle", "javascript", "python", "api"),
            Category.TRAVEL to listOf("travel", "flight", "flights", "itinerary", "hotel", "hotels", "airport", "vacation"),
            Category.SHOPPING to listOf("shopping", "discount", "checkout", "coupon", "buy"),
            Category.VIDEO to listOf("video", "trailer", "episode", "documentary"),
            Category.MUSIC to listOf("music", "song", "album", "playlist", "concert")
        )
        val TAG_TERMS = linkedMapOf(
            "Kotlin" to listOf("kotlin"), "Android" to listOf("android", "jetpack", "gradle"),
            "JavaScript" to listOf("javascript", "typescript"), "Python" to listOf("python"),
            "Web development" to listOf("html", "css", "frontend", "backend"),
            "Design" to listOf("design", "typography", "wireframe", "figma"),
            "Accessibility" to listOf("accessibility", "a11y", "screenreader"),
            "Recipes" to listOf("recipe", "recipes", "ingredients"),
            "Baking" to listOf("baking", "bake", "sourdough", "pastry"),
            "Vegetarian" to listOf("vegetarian"), "Vegan" to listOf("vegan"),
            "Travel planning" to listOf("itinerary", "vacation", "flights", "hotels"),
            "Photography" to listOf("photography", "photographer", "camera"),
            "Music" to listOf("playlist", "album", "concert"),
            "Books" to listOf("books", "novel", "reading")
        )
    }
}
