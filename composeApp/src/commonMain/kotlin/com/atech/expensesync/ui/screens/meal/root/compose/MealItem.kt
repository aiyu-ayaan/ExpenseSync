package com.atech.expensesync.ui.screens.meal.root.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Checklist
import androidx.compose.material.icons.twotone.EmojiFoodBeverage
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.atech.expensesync.component.DefaultCard
import com.atech.expensesync.component.mealIcons
import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.utils.getCurrentDayAndTotalDays
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun MealItem(
    modifier: Modifier = Modifier,
    state: MealBook,
    totalPrice: Double = 0.0,
    lastMonthPrice: Double = 100.0,
    onMealItemClick: () -> Unit = {},
    onlLongClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    DefaultCard(
        modifier = Modifier
            .combinedClickable(
                onClick = onMealItemClick,
                onLongClick = onlLongClick
            )
    ) {
        val (current, total) = getCurrentDayAndTotalDays()
        ListItem(
            modifier = modifier,
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            headlineContent = {
                Text(state.name)
            },
            supportingContent = if (totalPrice != 0.0) {
                {
                    Text(
                        "Bill this month: ${totalPrice.formatAmount()} ${state.defaultCurrency.symbol}"
                    )
                }
            } else if (lastMonthPrice != 0.0 && current == 1) {
                {
                    Text(
                        "Bill Last month: ${lastMonthPrice.formatAmount()} ${state.defaultCurrency.symbol}"
                    )
                }
            } else null,
            overlineContent = {
                Text("${current}/${total}")
            },
            leadingContent = {
                val icon =
                    (mealIcons.find { it.displayName == state.icon } ?: mealIcons.first()).icon
                Icon(
                    imageVector = icon,
                    contentDescription = "Meal Book"
                )
            },
            trailingContent = {
                FloatingActionButton(onClick = onActionClick, content = {
                    Icon(
                        imageVector = Icons.TwoTone.Checklist,
                        contentDescription = "Check Circle"
                    )
                })
            }
        )
    }
}

@Preview
@Composable
private fun MealItemPreview() {
    ExpenseSyncTheme {
        MealItem(
            state = MealBook(
                name = "Breakfast",
                description = "Subha ka khana",
                defaultPrice = 70.0
            )
        )
    }
}