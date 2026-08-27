package com.example.unibox.presentation.settings

import android.content.ActivityNotFoundException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Settings screen with data export, clear data, and app info.
 * UX fix #10: "Easy to sign up, impossible to leave" —
 * Data deletion and export are findable in under 3 taps.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val itemCount by viewModel.itemCount.collectAsState()
    val exportStatus by viewModel.exportStatus.collectAsState()
    val isExporting by viewModel.isExporting.collectAsState()
    val clearStatus by viewModel.clearStatus.collectAsState()
    val webPreviewStatus by viewModel.webPreviewStatus.collectAsState()
    val firecrawlEnabled by viewModel.firecrawlEnabled.collectAsState()
    val hasFirecrawlApiKey by viewModel.hasFirecrawlApiKey.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showClearDialog by remember { mutableStateOf(false) }
    var showFirecrawlDialog by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    val exportDocument = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
        viewModel::exportData
    )
    val context = LocalContext.current
    val versionName = remember(context) {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
    }

    // Show status messages via snackbar
    LaunchedEffect(exportStatus) {
        exportStatus?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearExportStatus()
        }
    }
    LaunchedEffect(clearStatus) {
        clearStatus?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearClearStatus()
        }
    }
    LaunchedEffect(webPreviewStatus) {
        webPreviewStatus?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearWebPreviewStatus()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier // 48dp default from Material
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // App info header
            Text(
                text = "UniBox $versionName",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Universal Smart Inbox",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            // Appearance section
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            val themeMode by viewModel.themeMode.collectAsState()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    com.example.unibox.domain.model.ThemeMode.SYSTEM to "System",
                    com.example.unibox.domain.model.ThemeMode.LIGHT to "Light",
                    com.example.unibox.domain.model.ThemeMode.DARK to "Dark"
                ).forEach { (mode, label) ->
                    androidx.compose.material3.FilterChip(
                        selected = themeMode == mode,
                        onClick = { viewModel.setThemeMode(mode) },
                        label = { 
                            Text(
                                text = label,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            ) 
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Web previews",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Enhanced extraction",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (firecrawlEnabled) {
                            "Saved links are sent to Firecrawl for readable content and richer metadata."
                        } else {
                            "Off by default. UniBox reads basic metadata directly from each page."
                        },
                        modifier = Modifier.padding(top = 4.dp, end = 16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    modifier = Modifier.semantics { contentDescription = "Enhanced web extraction" },
                    checked = firecrawlEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled) showFirecrawlDialog = true
                        else viewModel.setFirecrawlEnabled(false)
                    }
                )
            }

            TextButton(onClick = { showApiKeyDialog = true }) {
                Text(if (hasFirecrawlApiKey) "Manage personal API key" else "Add personal API key")
            }
            Text(
                text = "Optional for networks where keyless access is blocked. " +
                    "Keys stay encrypted on this device; authenticated requests use your Firecrawl credits.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            // Data Management section
            Text(
                text = "Data Management",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Items saved count
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp), // Minimum 48dp tap target
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Items saved",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$itemCount",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Export button
            OutlinedButton(
                onClick = {
                    try {
                        exportDocument.launch("unibox-export.json")
                    } catch (error: ActivityNotFoundException) {
                        viewModel.exportPickerUnavailable()
                    }
                },
                enabled = !isExporting,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FileDownload,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(if (isExporting) "Exporting library..." else "Export library (JSON)")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Choose where to save your notes, tags, and metadata. Image files and API keys are not included. JSON export cannot be restored in the app.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Clear data button
            OutlinedButton(
                onClick = { showClearDialog = true },
                enabled = !isExporting,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteForever,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Clear All Data")
            }

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            // About section
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "UniBox is a Universal Smart Inbox that centralizes all your saved links, screenshots, and notes from any app into one searchable place.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Built with Jetpack Compose, Room (FTS4), ML Kit, WorkManager, and Google Play Services.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Clear data confirmation dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear all data?") },
            text = {
                Text("This will delete all saved items and cannot be undone. Your exported JSON file will not be affected.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllData()
                        showClearDialog = false
                    }
                ) {
                    Text(
                        text = "Delete Everything",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showFirecrawlDialog) {
        AlertDialog(
            onDismissRequest = { showFirecrawlDialog = false },
            title = { Text("Enable enhanced previews?") },
            text = {
                Text(
                    "URLs you save or refresh will be sent to Firecrawl for extraction. " +
                        "URLs can contain sensitive query parameters, so avoid private links. " +
                        "UniBox will not send website cookies, website login credentials, local-network URLs, " +
                        "or common tracking parameters."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showFirecrawlDialog = false
                        viewModel.setFirecrawlEnabled(true)
                    }
                ) {
                    Text("Enable")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFirecrawlDialog = false }) {
                    Text("Keep off")
                }
            }
        )
    }

    if (showApiKeyDialog) {
        var apiKey by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showApiKeyDialog = false },
            title = { Text("Personal Firecrawl key") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enter your own Firecrawl API key. It is never included in exports or backups.")
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it.take(256) },
                        label = { Text("API key") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )
                    if (hasFirecrawlApiKey) {
                        TextButton(onClick = {
                            viewModel.saveFirecrawlApiKey(null)
                            showApiKeyDialog = false
                        }) {
                            Text("Remove saved key")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.saveFirecrawlApiKey(apiKey.trim())
                        showApiKeyDialog = false
                    },
                    enabled = apiKey.trim().isNotEmpty() && apiKey.trim().none(Char::isWhitespace)
                ) {
                    Text("Save key")
                }
            },
            dismissButton = {
                TextButton(onClick = { showApiKeyDialog = false }) { Text("Cancel") }
            }
        )
    }
}
