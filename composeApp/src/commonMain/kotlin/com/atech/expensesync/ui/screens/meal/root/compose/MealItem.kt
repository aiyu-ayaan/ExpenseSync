package com.atech.expensesync.ui.screens.meal.root.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Checklist
import androidx.compose.material.icons.twotone.EmojiFoodBeverage
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.atech.expensesync.component.DefaultCard
import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.utils.getCurrentDayAndTotalDays
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MealItem(
    modifier: Modifier = Modifier,
    state: MealBook,
    totalPrice: Double = 0.0,
    lastMonthPrice: Double = 100.0,
    onMealItemClick: () -> Unit = {}
) {
    DefaultCard(
        modifier = Modifier
            .clickable { onMealItemClick.invoke() }
    ) {
        val (current, total) = getCurrentDayAndTotalDays()
        Column {
            ListItem(
                modifier = modifier,
                text = {
                    Text(state.name)
                },
                secondaryText = if (totalPrice != 0.0) {
                    {
                        Text(
                            "Price till now: ${totalPrice.formatAmount()} ${state.defaultCurrency.symbol}"
                        )
                    }
                } else if (lastMonthPrice != 0.0 && current == 1) {
                    {
                        Text(
                            "Last month price: ${lastMonthPrice.formatAmount()} ${state.defaultCurrency.symbol}"
                        )
                    }
                } else null,
                overlineText = {
                    Text("${current}/${total}")
                },
                icon = {
                    Icon(
                        imageVector = Icons.TwoTone.EmojiFoodBeverage,
                        contentDescription = "Meal Book"
                    )
                },
                trailing = {
                    FloatingActionButton(onClick = {}, content = {
                        Icon(
                            imageVector = Icons.TwoTone.Checklist,
                            contentDescription = "Check Circle"
                        )
                    })
                }
            )
        }
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