package com.atech.expensesync.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.atech.expensesync.utils.Currency

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChooseCurrencyBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit = {},
    onCurrencyClick: (Currency) -> Unit = {}
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Currency.entries.toList().forEach {
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.clickable {
                        onCurrencyClick(it)
                        onDismissRequest()
                    },
                    headlineContent = {
                        Text(it.name)
                    },
                    leadingContent = {
                        Box(
                            modifier = Modifier.size(34.dp)
                                .background(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary
                                )
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = it.symbol,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                )
            }
        }
    }
}
