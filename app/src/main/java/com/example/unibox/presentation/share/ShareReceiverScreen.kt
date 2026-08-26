package com.example.unibox.presentation.share

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.rounded.AllInbox
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * Preview screen shown when content is shared to UniBox.
 * Displays the parsed shared data and lets the user save or discard.
 */
@Composable
fun ShareReceiverScreen(
    sharedData: SharedData,
    onSave: () -> Unit,
    onDiscard: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(
                initialOffsetY = { -it / 4 },
                animationSpec = tween(180)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AllInbox,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Save to UniBox",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "from ${formatPackageName(sharedData.sourcePackage)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Content type badge
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(160)
            ) + slideInVertically(
                initialOffsetY = { it / 3 },
                animationSpec = tween(200)
            )
        ) {
            Column {
                ContentTypeBadge(type = sharedData.type)

                Spacer(modifier = Modifier.height(16.dp))

                // Shared content preview card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(16.dp)
                ) {
                    Column {
                        // Subject / title if available
                        if (!sharedData.subject.isNullOrBlank()) {
                            Text(
                                text = sharedData.subject,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Image preview
                        if (sharedData.imageUri != null) {
                            AsyncImage(
                                model = sharedData.imageUri,
                                contentDescription = "Shared image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 10f)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            if (sharedData.imageUris.size > 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${sharedData.imageUris.size} images selected",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // URL extraction highlight
                        if (sharedData.url != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                    )
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Link,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = sharedData.url,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Raw text content
                        if (sharedData.rawText.isNotBlank()) {
                            Text(
                                text = "Raw Content:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = sharedData.rawText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 10,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Info: what will happen next
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                        )
                        .padding(14.dp)
                ) {
                    Text(
                        text = when (sharedData.type) {
                            SharedDataType.TEXT ->
                                if (sharedData.url != null)
                                    "UniBox will fetch the page title, description, and thumbnail in the background."
                                else
                                    "This text will be saved and made fully searchable."
                            SharedDataType.IMAGE, SharedDataType.MULTI_IMAGE ->
                                "Images are copied into UniBox and indexed with on-device text recognition."
                            SharedDataType.UNKNOWN ->
                                "This content type isn't fully supported yet."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDiscard,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Discard")
                    }

                    Button(
                        onClick = onSave,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Save,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save to Inbox")
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentTypeBadge(type: SharedDataType) {
    val icon: ImageVector = when (type) {
        SharedDataType.TEXT -> Icons.Outlined.Description
        SharedDataType.IMAGE, SharedDataType.MULTI_IMAGE -> Icons.Outlined.Image
        SharedDataType.UNKNOWN -> Icons.Outlined.Description
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = type.label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

/**
 * Convert a package name like "com.instagram.android" to "Instagram".
 */
private fun formatPackageName(pkg: String): String {
    val knownApps = mapOf(
        "com.instagram.android" to "Instagram",
        "com.twitter.android" to "Twitter",
        "com.google.android.apps.chrome" to "Chrome",
        "org.mozilla.firefox" to "Firefox",
        "com.zhiliaoapp.musically" to "TikTok",
        "com.reddit.frontpage" to "Reddit",
        "com.spotify.music" to "Spotify",
        "com.pinterest" to "Pinterest",
        "com.whatsapp" to "WhatsApp",
        "com.facebook.katana" to "Facebook",
        "com.linkedin.android" to "LinkedIn",
        "com.slack" to "Slack",
        "com.google.android.youtube" to "YouTube",
        "com.samsung.android.app.sbrowser" to "Samsung Browser",
        "com.brave.browser" to "Brave",
    )
    return knownApps[pkg]
        ?: pkg.split(".").lastOrNull { it != "android" && it.length > 2 }
            ?.replaceFirstChar { it.uppercase() }
        ?: pkg
}
