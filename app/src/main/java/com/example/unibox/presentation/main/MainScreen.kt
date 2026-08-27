package com.example.unibox.presentation.main

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.presentation.components.CategoryFilterRow
import com.example.unibox.presentation.components.LibrarySearchBar
import com.example.unibox.presentation.components.UniBoxCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onItemClick: (UniBoxItem) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val online by viewModel.isOnline.collectAsStateWithLifecycle()
    val capture by viewModel.manualSaveState.collectAsStateWithLifecycle()
    var showAdd by rememberSaveable { mutableStateOf(false) }
    var showFilters by rememberSaveable { mutableStateOf(false) }
    var draft by rememberSaveable { mutableStateOf("") }
    // Editing must update synchronously; the debounced result stream is not a text-field owner.
    var searchDraft by rememberSaveable { mutableStateOf(state.searchQuery) }
    var handledSavedId by rememberSaveable { mutableStateOf<Long?>(null) }
    val snackbar = remember { SnackbarHostState() }
    val clearFilters = {
        searchDraft = ""
        viewModel.clearWorkflowFilters()
    }

    LaunchedEffect(Unit) { viewModel.onSearchQueryChanged(searchDraft) }

    LaunchedEffect(capture.savedItemId) {
        if (capture.savedItemId != null && capture.savedItemId != handledSavedId) {
            handledSavedId = capture.savedItemId
            draft = ""
            showAdd = false
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.messages.collect { message ->
            val result = snackbar.showSnackbar(
                message.text, actionLabel = if (message.showLibrary) "View" else null,
                withDismissAction = true
            )
            if (result == SnackbarResult.ActionPerformed) {
                searchDraft = ""
                viewModel.showSavedItems()
            }
        }
    }

    Box(modifier.fillMaxSize()) {
        LibraryContent(
            uiState = state.copy(
                searchQuery = searchDraft,
                isSearching = state.isSearching || searchDraft != state.searchQuery
            ), online = online,
            onSearch = { searchDraft = it; viewModel.onSearchQueryChanged(it) },
            onCategory = viewModel::onCategorySelected,
            onCollection = viewModel::onCollectionSelected,
            onSection = viewModel::onSectionSelected,
            onClear = clearFilters,
            onFilters = { showFilters = true }, onItem = onItemClick,
            onFavorite = viewModel::toggleFavorite, onSave = viewModel::moveToLibrary,
            onSettings = onSettingsClick
        )
        ExtendedFloatingActionButton(
            onClick = { viewModel.clearCaptureError(); showAdd = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
                .semantics { contentDescription = "Add an item" },
            icon = { Icon(Icons.Outlined.Add, null) }, text = { Text("Add item") },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter).padding(bottom = 88.dp))
    }
    if (showFilters) {
        WorkflowFilterSheet(
            state, { showFilters = false }, viewModel::onSortOrderSelected,
            viewModel::onFavoriteOnlyChanged, viewModel::onCollectionSelected,
            viewModel::onCategorySelected, clearFilters
        )
    }
    if (showAdd) {
        ModalBottomSheet(
            onDismissRequest = { if (!capture.isSaving) showAdd = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                Modifier.fillMaxWidth().imePadding().verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp).padding(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Add to UniBox", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Save a thought or paste a link. You can organize it later.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = draft, onValueChange = { draft = it; viewModel.clearCaptureError() },
                    label = { Text("Note or link") }, modifier = Modifier.fillMaxWidth(),
                    minLines = 3, maxLines = 6, enabled = !capture.isSaving,
                    isError = capture.error != null,
                    supportingText = capture.error?.let { error -> { Text(error) } },
                    shape = RoundedCornerShape(12.dp)
                )
                Button(
                    onClick = { viewModel.saveManualItem(draft.trim()) },
                    enabled = draft.isNotBlank() && !capture.isSaving,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text(if (capture.isSaving) "Saving…" else "Save to inbox") }
            }
        }
    }
}

/** Stateless browsing surface, also exercised by the device UI tests. */
@Composable
internal fun LibraryContent(
    uiState: MainUiState, online: Boolean = true,
    onSearch: (String) -> Unit = {}, onCategory: (Category?) -> Unit = {},
    onCollection: (String?) -> Unit = {}, onSection: (LibrarySection) -> Unit = {},
    onClear: () -> Unit = {}, onFilters: () -> Unit = {},
    onItem: (UniBoxItem) -> Unit = {}, onFavorite: (UniBoxItem) -> Unit = {},
    onSave: (UniBoxItem) -> Unit = {}, onSettings: () -> Unit = {}
) {
    // Start a new browsing context at its first result, without jumping on ordinary item updates.
    val gridState = rememberSaveable(
        uiState.section, uiState.searchQuery, uiState.selectedCategory,
        uiState.selectedCollection, uiState.favoriteOnly, uiState.sortOrder,
        saver = LazyGridState.Saver
    ) { LazyGridState() }

    Column(Modifier.fillMaxSize()) {
        LibraryHeader(uiState, onSettings)
        SectionSwitcher(uiState.section, uiState.sectionCounts, onSection)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(280.dp), state = gridState,
            modifier = Modifier.weight(1f).testTag("library-grid"),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 104.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "browse-controls", span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    LibrarySearchBar(
                        uiState.searchQuery, onSearch, Modifier.padding(top = 8.dp),
                        placeholder = "Search this " + uiState.section.label.lowercase()
                    )
                    if (!online) {
                        Row(Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.CloudOff, null, Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Offline · saved items are available", Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    if (uiState.collectionSummaries.isNotEmpty() || uiState.selectedCollection != null) {
                        CollectionStrip(uiState, onCollection)
                    }
                    CategoryFilterRow(uiState.selectedCategory, onCategory, categories = uiState.availableCategories)
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            if (uiState.isSearching) "Searching…" else itemCountLabel(uiState.items.size),
                            Modifier.weight(1f), style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (uiState.hasActiveFilters) TextButton(onClear) { Text("Clear") }
                        OutlinedButton(
                            onFilters, shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Outlined.Tune, null, Modifier.size(18.dp))
                            Text(
                                if (uiState.activeFilterCount == 0) "Filters" else "Filters · " + uiState.activeFilterCount,
                                Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                    if (uiState.isSearching || uiState.isLoading) {
                        LinearProgressIndicator(Modifier.fillMaxWidth().height(2.dp))
                    }
                }
            }
            if (!uiState.isLoading && !uiState.isSearching &&
                (uiState.items.isEmpty() || uiState.errorMessage != null)) {
                item(key = "empty-state", span = { GridItemSpan(maxLineSpan) }) {
                    EmptyLibraryState(uiState, onClear)
                }
            } else {
                items(uiState.items, key = { it.id }, contentType = { "saved-item" }) { item ->
                    UniBoxCard(item, onClick = onItem, onToggleFavorite = onFavorite, onMoveToLibrary = onSave)
                }
            }
        }
    }
}

@Composable
private fun LibraryHeader(state: MainUiState, onSettings: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp, top = 10.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(state.section.label, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
            Text(
                when (state.section) {
                    LibrarySection.INBOX -> if (state.sectionItemCount == 0) "A little room for what comes next" else "Keep what matters. Let the rest go."
                    LibrarySection.LIBRARY -> "Your ideas, in good company"
                    LibrarySection.ARCHIVE -> "Out of the way. Still within reach."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onSettings) { Icon(Icons.Outlined.Settings, "Settings") }
    }
}

@Composable
private fun SectionSwitcher(
    selected: LibrarySection, counts: Map<LibrarySection, Int>, onSelect: (LibrarySection) -> Unit
) {
    Surface(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            LibrarySection.entries.forEach { section ->
                Surface(
                    Modifier.weight(1f),
                    color = if (selected == section) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Box(
                        Modifier.selectable(selected == section, role = Role.Tab, onClick = { onSelect(section) })
                            .heightIn(min = 44.dp).padding(horizontal = 4.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            section.label + " · " + (counts[section] ?: 0),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (selected == section) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (selected == section) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionStrip(state: MainUiState, onSelect: (String?) -> Unit) {
    Column {
        Text("Collections", Modifier.padding(top = 8.dp), style = MaterialTheme.typography.labelMedium)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(state.selectedCollection == null, { onSelect(null) }, label = { Text("All collections") })
            val collections = state.collectionSummaries.toMutableList()
            state.selectedCollection?.let { selected ->
                if (collections.none { it.name == selected }) collections.add(CollectionSummary(selected, 0))
            }
            collections.forEach { collection ->
                FilterChip(state.selectedCollection == collection.name, { onSelect(collection.name) },
                    label = { Text(collection.name + " · " + collection.count) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun WorkflowFilterSheet(
    state: MainUiState, onDismiss: () -> Unit, onSort: (ItemSortOrder) -> Unit,
    onFavorite: (Boolean) -> Unit, onCollection: (String?) -> Unit,
    onCategory: (Category?) -> Unit, onClear: () -> Unit
) {
    ModalBottomSheet(onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp).padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Browse options", Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
                TextButton(onClear, enabled = state.hasActiveFilters) { Text("Reset all") }
            }
            Row(
                Modifier.fillMaxWidth().toggleable(state.favoriteOnly, role = Role.Switch, onValueChange = onFavorite),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Favorites only", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                Switch(state.favoriteOnly, onCheckedChange = null)
            }
            HorizontalDivider()
            FilterGroup("Sort") {
                ItemSortOrder.entries.forEach { sort ->
                    FilterChip(state.sortOrder == sort, { onSort(sort) }, label = { Text(sort.label) })
                }
            }
            FilterGroup("Category") {
                FilterChip(state.selectedCategory == null, { onCategory(null) }, label = { Text("All categories") })
                state.availableCategories.forEach { category ->
                    FilterChip(state.selectedCategory == category, { onCategory(category) }, label = { Text(category.displayName) })
                }
            }
            if (state.collections.isNotEmpty() || state.selectedCollection != null) {
                FilterGroup("Collection") {
                    FilterChip(state.selectedCollection == null, { onCollection(null) }, label = { Text("Any collection") })
                    (state.collections + listOfNotNull(state.selectedCollection)).distinct().forEach { name ->
                        FilterChip(state.selectedCollection == name, { onCollection(name) }, label = { Text(name) })
                    }
                }
            }
            Button(onDismiss, Modifier.fillMaxWidth().heightIn(min = 48.dp), shape = RoundedCornerShape(12.dp)) {
                Text("Show results")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterGroup(title: String, content: @Composable FlowRowScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
private fun EmptyLibraryState(state: MainUiState, onClear: () -> Unit) {
    Surface(
        Modifier.fillMaxWidth().padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                when {
                    state.errorMessage != null -> "Could not load items"
                    state.hasActiveFilters -> "No matches in this " + state.section.label.lowercase()
                    state.section == LibrarySection.INBOX -> "Your inbox is clear"
                    state.section == LibrarySection.ARCHIVE -> "Nothing archived yet"
                    else -> "Make room for a good idea"
                },
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                state.errorMessage ?: when {
                    state.hasActiveFilters -> "Try fewer words, clear your filters, or look in another tab."
                    state.section == LibrarySection.INBOX -> "Add a note or share something from another app to get started."
                    state.section == LibrarySection.ARCHIVE -> "Archive an item when you are done with it. It stays searchable here."
                    else -> "Keep an item from your inbox to find it here, along with its tags and collection."
                },
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (state.hasActiveFilters) TextButton(onClear) { Text("Clear search and filters") }
        }
    }
}

private fun itemCountLabel(count: Int): String = when (count) {
    0 -> "No items"
    1 -> "1 item"
    else -> "$count items"
}
