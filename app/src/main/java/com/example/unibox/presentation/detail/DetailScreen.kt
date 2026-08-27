package com.example.unibox.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.ItemStatus
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.model.WebEnrichmentStatus
import com.example.unibox.domain.organization.OrganizationSelection
import com.example.unibox.domain.organization.OrganizationSuggestions
import com.example.unibox.presentation.components.CategoryChip
import com.example.unibox.presentation.components.SkeletonDetailScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditSheet by remember { mutableStateOf(false) }
    var showSnoozeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.actionMessage) {
        uiState.actionMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearActionMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Item") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back")
                    }
                },
                actions = {
                    uiState.item?.let { item ->
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                imageVector = if (item.isFavorite) {
                                    Icons.Outlined.Star
                                } else {
                                    Icons.Outlined.StarBorder
                                },
                                contentDescription = if (item.isFavorite) {
                                    "Remove from favorites"
                                } else {
                                    "Add to favorites"
                                }
                            )
                        }
                        IconButton(onClick = { showEditSheet = true }) {
                            Icon(Icons.Outlined.Edit, "Edit item")
                        }
                        if (item.status == ItemStatus.INBOX) {
                            IconButton(onClick = viewModel::moveToLibrary) {
                                Icon(Icons.Outlined.Done, "Save to library")
                            }
                        }
                        IconButton(onClick = viewModel::toggleArchived) {
                            Icon(
                                imageVector = if (item.status == ItemStatus.ARCHIVED) {
                                    Icons.Outlined.Unarchive
                                } else {
                                    Icons.Outlined.Archive
                                },
                                contentDescription = if (item.status == ItemStatus.ARCHIVED) {
                                    "Restore to inbox"
                                } else {
                                    "Archive"
                                }
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> SkeletonDetailScreen(
                modifier = Modifier.padding(innerPadding)
            )
            uiState.item == null -> MissingItemState(
                onNavigateBack = onNavigateBack,
                modifier = Modifier.padding(innerPadding)
            )
            else -> ItemDetails(
                item = requireNotNull(uiState.item),
                onOpenLink = { url ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                },
                onSnooze = { showSnoozeDialog = true },
                onRetryWebPreview = viewModel::retryWebPreview,
                suggestions = uiState.organizationSuggestions,
                isApplyingOrganization = uiState.isApplyingOrganization,
                onApplyOrganization = viewModel::applyOrganizationSuggestions,
                onDismissOrganization = viewModel::dismissOrganizationSuggestions,
                onReviewOrganization = viewModel::reviewOrganizationSuggestions,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    val item = uiState.item
    if (showEditSheet && item != null) {
        EditItemSheet(
            item = item,
            existingCollections = uiState.collections,
            onDismiss = { showEditSheet = false },
            onSave = { title, description, note, category, collection, tags ->
                viewModel.updateItemDetails(
                    title = title,
                    description = description,
                    userNote = note,
                    category = category,
                    collectionName = collection,
                    tags = tags
                )
                showEditSheet = false
            }
        )
    }

    if (showSnoozeDialog && item != null) {
        SnoozeDialog(
            isSnoozed = item.snoozedUntil != null,
            onDismiss = { showSnoozeDialog = false },
            onSnooze = { timestamp ->
                viewModel.snoozeUntil(timestamp)
                showSnoozeDialog = false
            },
            onClear = {
                viewModel.clearSnooze()
                showSnoozeDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete this item?") },
            text = { Text("The saved item and its local images will be removed permanently.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteItem(onNavigateBack)
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun ItemDetails(
    item: UniBoxItem,
    onOpenLink: (String) -> Unit,
    onSnooze: () -> Unit,
    onRetryWebPreview: () -> Unit,
    suggestions: OrganizationSuggestions,
    isApplyingOrganization: Boolean,
    onApplyOrganization: (OrganizationSelection) -> Unit,
    onDismissOrganization: () -> Unit,
    onReviewOrganization: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        val localImages = item.imageUris.ifEmpty { listOfNotNull(item.imageUri) }
        val images = if (localImages.isNotEmpty()) localImages else listOfNotNull(item.thumbnailUrl)

        if (images.isNotEmpty()) {
            item {
                if (images.size == 1) {
                    AsyncImage(
                        model = images.first(),
                        contentDescription = item.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(images) { image ->
                            AsyncImage(
                                model = image,
                                contentDescription = item.title,
                                modifier = Modifier
                                    .width(290.dp)
                                    .aspectRatio(4f / 3f),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryChip(category = item.category)
                    Text(
                        text = formatDate(item.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (item.description.isNotBlank()) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                MetadataRow(item)

                if (!suggestions.isEmpty) {
                    if (item.organizationReviewed) {
                        TextButton(onClick = onReviewOrganization) { Text("Review suggestions") }
                    } else {
                        OrganizationSuggestionsCard(
                            suggestions = suggestions,
                            isApplying = isApplyingOrganization,
                            onApply = onApplyOrganization,
                            onDismiss = onDismissOrganization
                        )
                    }
                }

                if (item.url != null) {
                    WebPreviewPanel(item = item, onRetry = onRetryWebPreview)
                }

                if (item.userNote.isNotBlank()) {
                    DetailSection(title = "Your note") {
                        Text(
                            text = item.userNote,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                if (!item.extractedText.isNullOrBlank()) {
                    DetailSection(
                        title = if (item.url != null && item.imageUri == null && item.imageUris.isEmpty()) {
                            "Readable page content"
                        } else {
                            "Text found in this item"
                        }
                    ) {
                        Text(
                            text = item.extractedText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 12,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                item.url?.let { url ->
                    Button(
                        onClick = { onOpenLink(url) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text("Open original", modifier = Modifier.padding(start = 8.dp))
                    }
                }

                OutlinedButton(
                    onClick = onSnooze,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Outlined.Schedule, null, modifier = Modifier.size(18.dp))
                    Text(
                        text = item.snoozedUntil?.let { "Snoozed until ${formatDateTime(it)}" }
                            ?: "Snooze for later",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                if (item.latitude != null && item.longitude != null) {
                    DetailSection(title = "Location reminder") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = item.locationLabel ?: "Attached location",
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WebPreviewPanel(
    item: UniBoxItem,
    onRetry: () -> Unit
) {
    if (item.enrichmentStatus == WebEnrichmentStatus.NOT_REQUIRED) return

    val title = when (item.enrichmentStatus) {
        WebEnrichmentStatus.PENDING -> "Building preview"
        WebEnrichmentStatus.COMPLETE -> "Readable web preview"
        WebEnrichmentStatus.PARTIAL -> "Basic web preview"
        WebEnrichmentStatus.FAILED -> "Preview unavailable"
        WebEnrichmentStatus.NOT_REQUIRED -> return
    }
    val supportingText = when (item.enrichmentStatus) {
        WebEnrichmentStatus.PENDING -> "UniBox is reading this page in the background."
        WebEnrichmentStatus.COMPLETE -> "The page content is available for search."
        WebEnrichmentStatus.PARTIAL -> item.enrichmentError
            ?: "Basic metadata is available. Enhanced extraction can be enabled in Settings."
        WebEnrichmentStatus.FAILED -> item.enrichmentError
            ?: "UniBox could not read this page."
        WebEnrichmentStatus.NOT_REQUIRED -> ""
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Language,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = supportingText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (item.enrichmentStatus == WebEnrichmentStatus.PENDING) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            val webMetadata = buildList {
                item.webSiteName?.let(::add)
                item.webAuthor?.let { add("By $it") }
                item.webReadingTimeMinutes?.let { add("$it min read") }
                item.webLanguage?.let { add(it.uppercase()) }
                item.webPublishedAt?.take(10)?.let(::add)
            }
            if (webMetadata.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    webMetadata.forEach { MetadataChip(it) }
                }
            }

            if (item.enrichmentStatus in setOf(
                    WebEnrichmentStatus.PARTIAL,
                    WebEnrichmentStatus.FAILED
                )
            ) {
                TextButton(onClick = onRetry) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("Refresh preview", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun MetadataRow(item: UniBoxItem) {
    val metadata = buildList {
        item.collectionName?.let { add(it) }
        addAll(item.tags)
    }
    if (metadata.isEmpty()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        metadata.forEach { label -> MetadataChip(label) }
    }
}

@Composable
private fun MetadataChip(label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditItemSheet(
    item: UniBoxItem,
    existingCollections: List<String>,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Category, String?, List<String>) -> Unit
) {
    var title by remember(item.id) { mutableStateOf(item.title) }
    var description by remember(item.id) { mutableStateOf(item.description) }
    var note by remember(item.id) { mutableStateOf(item.userNote) }
    var category by remember(item.id) { mutableStateOf(item.category) }
    var collection by remember(item.id) { mutableStateOf(item.collectionName.orEmpty()) }
    var tags by remember(item.id) { mutableStateOf(item.tags.joinToString(", ")) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Text("Edit item", style = MaterialTheme.typography.titleLarge) }
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Your note") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )
            }
            item {
                Text("Category", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Category.entries.forEach { option ->
                        FilterChip(
                            selected = category == option,
                            onClick = { category = option },
                            label = { Text(option.displayName) }
                        )
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = collection,
                    onValueChange = { collection = it },
                    label = { Text("Collection") },
                    supportingText = { Text("Choose an existing name or create a new one") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                if (existingCollections.isNotEmpty()) {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        existingCollections.forEach { name ->
                            FilterChip(
                                selected = collection == name,
                                onClick = { collection = name },
                                label = { Text(name) }
                            )
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags") },
                    supportingText = { Text("Separate tags with commas") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
            item { HorizontalDivider() }
            item {
                Button(
                    onClick = {
                        onSave(
                            title,
                            description,
                            note,
                            category,
                            collection.takeIf { it.isNotBlank() },
                            tags.split(",")
                        )
                    },
                    enabled = title.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save changes")
                }
            }
        }
    }
}

@Composable
private fun SnoozeDialog(
    isSnoozed: Boolean,
    onDismiss: () -> Unit,
    onSnooze: (Long) -> Unit,
    onClear: () -> Unit
) {
    val now = System.currentTimeMillis()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Snooze item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SnoozeChoice("Tomorrow", now + DAY_MILLIS, onSnooze)
                SnoozeChoice("In three days", now + 3 * DAY_MILLIS, onSnooze)
                SnoozeChoice("Next week", now + 7 * DAY_MILLIS, onSnooze)
            }
        },
        confirmButton = {
            if (isSnoozed) {
                TextButton(onClick = onClear) { Text("Remove snooze") }
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun SnoozeChoice(label: String, timestamp: Long, onClick: (Long) -> Unit) {
    TextButton(
        onClick = { onClick(timestamp) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun MissingItemState(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Item not found", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onNavigateBack) { Text("Go back") }
        }
    }
}

private fun formatDate(timestamp: Long): String =
    SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(timestamp))

private fun formatDateTime(timestamp: Long): String =
    SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(timestamp))

private const val DAY_MILLIS = 24L * 60L * 60L * 1_000L
