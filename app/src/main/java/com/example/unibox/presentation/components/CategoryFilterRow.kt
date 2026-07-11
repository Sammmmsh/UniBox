package com.example.unibox.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.unibox.domain.model.Category

/**
 * Horizontally scrollable row of filter chips for content categories.
 * "All" is a special entry that maps to null selection (show everything).
 */
@Composable
fun CategoryFilterRow(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All" chip
        FilterChipItem(
            label = "All",
            emoji = "✨",
            isSelected = selectedCategory == null,
            selectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            selectedTextColor = MaterialTheme.colorScheme.primary,
            onClick = { onCategorySelected(null) }
        )

        // Category chips
        Category.entries.forEach { category ->
            FilterChipItem(
                label = category.displayName,
                emoji = category.emoji,
                isSelected = selectedCategory == category,
                selectedColor = category.color.copy(alpha = 0.15f),
                selectedTextColor = category.color,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    emoji: String,
    isSelected: Boolean,
    selectedColor: androidx.compose.ui.graphics.Color,
    selectedTextColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) selectedColor
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "chipBg"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) selectedTextColor
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "chipText"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}
