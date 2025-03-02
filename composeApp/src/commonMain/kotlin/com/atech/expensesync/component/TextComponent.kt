package com.atech.expensesync.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.atech.expensesync.ui.theme.captionColor
import com.atech.expensesync.ui.theme.spacing

@Composable
fun TitleComposable(
    title: String,
    modifier: Modifier = Modifier,
    padding: Dp = MaterialTheme.spacing.default
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.captionColor
    )
}

@Composable
fun DisplayCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    border: BorderStroke = CardDefaults.outlinedCardBorder(),
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clickable { onClick.invoke() }) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            border = border
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                content = content
            )
        }
    }
}