package com.example.unibox.domain.organization

import com.example.unibox.domain.model.Category

data class OrganizationHint<T>(val value: T, val reason: String)

data class OrganizationSuggestions(
    val category: OrganizationHint<Category>? = null,
    val tags: List<OrganizationHint<String>> = emptyList(),
    val collection: OrganizationHint<String>? = null
) {
    val isEmpty: Boolean get() = category == null && tags.isEmpty() && collection == null
}

data class OrganizationSelection(
    val category: Category? = null,
    val tags: List<String> = emptyList(),
    val collectionName: String? = null
) {
    val isEmpty: Boolean get() = category == null && tags.isEmpty() && collectionName == null
}
