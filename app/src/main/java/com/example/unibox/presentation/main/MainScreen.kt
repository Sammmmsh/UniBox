package com.example.unibox.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.presentation.components.CategoryFilterRow
import com.example.unibox.presentation.components.LibrarySearchBar
import com.example.unibox.presentation.components.SkeletonGrid
import com.example.unibox.presentation.components.UniBoxCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onItemClick: (UniBoxItem) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    var showAddSheet by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var manualText by remember { mutableStateOf("") }
    val addSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LibraryHeader(
                section = uiState.section,
                itemCount = uiState.items.size,
                onSettingsClick = onSettingsClick
            )

            SectionSwitcher(
                selectedSection = uiState.section,
                onSectionSelected = viewModel::onSectionSelected
            )

            AnimatedVisibility(
                visible = !isOnline,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CloudOff,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Offline — your saved library is still available",
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            LibrarySearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged
            )

            CategoryFilterRow(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::onCategorySelected
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = itemCountLabel(uiState.items.size),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = { showFilterSheet = true },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(
                        text = if (uiState.activeFilterCount > 0) {
                            "Filters · ${uiState.activeFilterCount}"
                        } else {
                            "Filters"
                        },
                        modifier = Modifier.padding(start = 7.dp)
                    )
                }
            }

            when {
                uiState.isLoading -> SkeletonGrid(modifier = Modifier.weight(1f))
                uiState.items.isEmpty() -> EmptyLibraryState(
                    hasQuery = uiState.searchQuery.isNotBlank() ||
                        uiState.selectedCategory != null ||
                        uiState.activeFilterCount > 0,
                    section = uiState.section,
                    modifier = Modifier.weight(1f)
                )
                else -> LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(minSize = 280.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 8.dp,
                        bottom = 96.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp
                ) {
                    items(items = uiState.items, key = { it.id }) { item ->
                        UniBoxCard(
                            item = item,
                            onClick = { onItemClick(item) },
                            onToggleFavorite = viewModel::toggleFavorite,
                            onMoveToLibrary = viewModel::moveToLibrary
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add an item",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    if (showFilterSheet) {
        WorkflowFilterSheet(
            uiState = uiState,
            onDismiss = { showFilterSheet = false },
            onSortOrderSelected = viewModel::onSortOrderSelected,
            onFavoriteOnlyChanged = viewModel::onFavoriteOnlyChanged,
            onCollectionSelected = viewModel::onCollectionSelected,
            onClear = viewModel::clearWorkflowFilters,
            sheetState = filterSheetState
        )
    }

    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            sheetState = addSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Add to UniBox", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "Save a thought or paste a link. UniBox will index it locally.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedTextField(
                    value = manualText,
                    onValueChange = { manualText = it },
                    label = { Text("Note or link") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = {
                        viewModel.saveManualItem(manualText)
                        manualText = ""
                        showAddSheet = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = manualText.isNotBlank(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save to inbox")
                }
            }
        }
    }
}

@Composable
private fun LibraryHeader(
    section: LibrarySection,
    itemCount: Int,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 8.dp, top = 14.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = section.label,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = sectionSubtitle(section, itemCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SectionSwitcher(
    selectedSection: LibrarySection,
    onSectionSelected: (LibrarySection) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LibrarySection.entries.forEach { section ->
            FilterChip(
                selected = section == selectedSection,
                onClick = { onSectionSelected(section) },
                label = { Text(section.label) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = section == selectedSection,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkflowFilterSheet(
    uiState: MainUiState,
    onDismiss: () -> Unit,
    onSortOrderSelected: (ItemSortOrder) -> Unit,
    onFavoriteOnlyChanged: (Boolean) -> Unit,
    onCollectionSelected: (String?) -> Unit,
    onClear: () -> Unit,
    sheetState: androidx.compose.material3.SheetState
) {
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Filter library",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onClear, enabled = uiState.activeFilterCount > 0) {
                    Text("Reset")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFavoriteOnlyChanged(!uiState.favoriteOnly) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Favorites only", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Show only items you have starred",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = uiState.favoriteOnly,
                    onCheckedChange = onFavoriteOnlyChanged
                )
            }

            HorizontalDivider()

            FilterGroup(title = "Sort") {
                ItemSortOrder.entries.forEach { sortOrder ->
                    CompactFilterChip(
                        label = sortOrder.label,
                        selected = uiState.sortOrder == sortOrder,
                        onClick = { onSortOrderSelected(sortOrder) }
                    )
                }
            }

            FilterGroup(title = "Collection") {
                CompactFilterChip(
                    label = "Any collection",
                    selected = uiState.selectedCollection == null,
                    onClick = { onCollectionSelected(null) }
                )
                uiState.collections.forEach { collection ->
                    CompactFilterChip(
                        label = collection,
                        selected = uiState.selectedCollection == collection,
                        onClick = { onCollectionSelected(collection) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterGroup(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun CompactFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) }
    )
}

@Composable
private fun EmptyLibraryState(
    hasQuery: Boolean,
    section: LibrarySection,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Inbox,
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when {
                    hasQuery -> "Nothing found"
                    section == LibrarySection.INBOX -> "Your inbox is clear"
                    section == LibrarySection.ARCHIVE -> "Nothing archived yet"
                    else -> "Your library is empty"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = when {
                    hasQuery -> "Try another search or reset the current filters."
                    section == LibrarySection.INBOX -> "New saves will wait here until you organize or archive them."
                    section == LibrarySection.ARCHIVE -> "Archived items remain searchable and can be restored."
                    else -> "Share a link, image, or note from any app to keep it here."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun sectionSubtitle(section: LibrarySection, itemCount: Int): String = when (section) {
    LibrarySection.INBOX -> if (itemCount == 0) "Nothing needs your attention" else "Decide what is worth keeping"
    LibrarySection.LIBRARY -> "Everything you have kept"
    LibrarySection.ARCHIVE -> "Out of sight, never lost"
}

private fun itemCountLabel(count: Int): String = when (count) {
    0 -> "No items"
    1 -> "1 item"
    else -> "$count items"
}
