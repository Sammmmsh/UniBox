package com.example.unibox.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.unibox.domain.organization.OrganizationSelection
import com.example.unibox.domain.organization.OrganizationSuggestions

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun OrganizationSuggestionsCard(
    suggestions: OrganizationSuggestions,
    isApplying: Boolean,
    onApply: (OrganizationSelection) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (suggestions.isEmpty) return
    var categorySelected by rememberSaveable(suggestions) { mutableStateOf(true) }
    var collectionSelected by rememberSaveable(suggestions) { mutableStateOf(true) }
    var selectedTags by rememberSaveable(suggestions) {
        mutableStateOf(suggestions.tags.map { it.value })
    }
    val selection = OrganizationSelection(
        category = suggestions.category?.value?.takeIf { categorySelected },
        tags = selectedTags,
        collectionName = suggestions.collection?.value?.takeIf { collectionSelected }
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Suggested organization",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Choose what fits. Suggestions are made on this device.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            suggestions.category?.let { hint ->
                Column {
                    SuggestionLabel("Category")
                    FilterChip(
                        selected = categorySelected,
                        onClick = { categorySelected = !categorySelected },
                        label = { Text(hint.value.displayName) },
                        enabled = !isApplying
                    )
                    SuggestionReason(hint.reason)
                }
            }
            if (suggestions.tags.isNotEmpty()) {
                Column {
                    SuggestionLabel("Tags")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        suggestions.tags.forEach { hint ->
                            FilterChip(
                                selected = hint.value in selectedTags,
                                onClick = {
                                    selectedTags = if (hint.value in selectedTags) {
                                        selectedTags - hint.value
                                    } else selectedTags + hint.value
                                },
                                label = { Text(hint.value) },
                                modifier = Modifier.semantics {
                                    contentDescription = hint.value + ". " + hint.reason
                                },
                                enabled = !isApplying
                            )
                        }
                    }
                }
            }
            suggestions.collection?.let { hint ->
                Column {
                    SuggestionLabel("Collection")
                    FilterChip(
                        selected = collectionSelected,
                        onClick = { collectionSelected = !collectionSelected },
                        label = { Text(hint.value) },
                        enabled = !isApplying
                    )
                    SuggestionReason(hint.reason)
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onApply(selection) },
                    enabled = !isApplying && !selection.isEmpty,
                    modifier = Modifier.heightIn(min = 48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isApplying) "Applying…" else "Apply selected")
                }
                TextButton(onClick = onDismiss, enabled = !isApplying) {
                    Text("Dismiss")
                }
            }
        }
    }
}

@Composable
private fun SuggestionLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelLarge)
}

@Composable
private fun SuggestionReason(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
