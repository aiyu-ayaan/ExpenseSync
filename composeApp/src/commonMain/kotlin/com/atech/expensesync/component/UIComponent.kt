package com.atech.expensesync.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.atech.expensesync.ui.theme.captionColor
import com.atech.expensesync.ui.theme.spacing

@Composable
fun TitleComposable(
    title: String, modifier: Modifier = Modifier, padding: Dp = MaterialTheme.spacing.default
) {
    Text(
        modifier = modifier.fillMaxWidth().padding(padding),
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.captionColor
    )
}

@Composable
fun TextItem(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit = {},
    endIcon: ImageVector? = null,
    onEndIconClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.background(containerColor).clickable { onClick() }) {
        Row(
            modifier = Modifier.background(containerColor).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier.padding(MaterialTheme.spacing.medium),
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.87f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (endIcon != null) IconButton(onClick = { onEndIconClick?.invoke() }) {
                Icon(imageVector = endIcon, contentDescription = null)
            }
        }
    }
}

@Composable
fun DisplayCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    border: BorderStroke = CardDefaults.outlinedCardBorder(),
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Surface(
        modifier = modifier.clickable { onClick.invoke() }) {
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(), border = border
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.medium),
                content = content
            )
        }
    }
}

/**
 * Bottom padding
 * This is a bottom padding that is used to add padding to the bottom
 */
@Composable
fun BottomPadding(
    padding: Dp = MaterialTheme.spacing.bottomPadding
) {
    Spacer(
        modifier = Modifier.height(
            padding
        )
    )
}


/**
 * Bottom padding lazy
 * This is a bottom padding lazy that is used to add padding to the bottom
 * @param key String key
 */
fun LazyListScope.bottomPaddingLazy(key: String = "bottom_padding") {
    item(
        key = key
    ) {
        BottomPadding()
    }
}