package com.atech.expensesync.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountBalanceWallet
import androidx.compose.material.icons.twotone.AttachMoney
import androidx.compose.material.icons.twotone.BakeryDining
import androidx.compose.material.icons.twotone.BreakfastDining
import androidx.compose.material.icons.twotone.Cake
import androidx.compose.material.icons.twotone.ChildCare
import androidx.compose.material.icons.twotone.Coffee
import androidx.compose.material.icons.twotone.Cookie
import androidx.compose.material.icons.twotone.CreditCard
import androidx.compose.material.icons.twotone.DirectionsCar
import androidx.compose.material.icons.twotone.ElectricBolt
import androidx.compose.material.icons.twotone.EmojiFoodBeverage
import androidx.compose.material.icons.twotone.Fastfood
import androidx.compose.material.icons.twotone.FitnessCenter
import androidx.compose.material.icons.twotone.Flight
import androidx.compose.material.icons.twotone.Grass
import androidx.compose.material.icons.twotone.HealthAndSafety
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Icecream
import androidx.compose.material.icons.twotone.LocalAtm
import androidx.compose.material.icons.twotone.LocalCafe
import androidx.compose.material.icons.twotone.LocalDining
import androidx.compose.material.icons.twotone.LocalGroceryStore
import androidx.compose.material.icons.twotone.LocalPizza
import androidx.compose.material.icons.twotone.LunchDining
import androidx.compose.material.icons.twotone.MonetizationOn
import androidx.compose.material.icons.twotone.MusicNote
import androidx.compose.material.icons.twotone.Nightlife
import androidx.compose.material.icons.twotone.Paid
import androidx.compose.material.icons.twotone.Pets
import androidx.compose.material.icons.twotone.RamenDining
import androidx.compose.material.icons.twotone.Receipt
import androidx.compose.material.icons.twotone.Restaurant
import androidx.compose.material.icons.twotone.RiceBowl
import androidx.compose.material.icons.twotone.Savings
import androidx.compose.material.icons.twotone.School
import androidx.compose.material.icons.twotone.SetMeal
import androidx.compose.material.icons.twotone.ShoppingCart
import androidx.compose.material.icons.twotone.SoupKitchen
import androidx.compose.material.icons.twotone.Spa
import androidx.compose.material.icons.twotone.SportsEsports
import androidx.compose.material.icons.twotone.TheaterComedy
import androidx.compose.material.icons.twotone.WineBar
import androidx.compose.material.icons.twotone.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class IconItem(val icon: ImageVector, val displayName: String)

val expenseIcons = listOf(
    Icons.TwoTone.AttachMoney to "Money",
    Icons.TwoTone.CreditCard to "Credit Card",
    Icons.TwoTone.Receipt to "Receipt",
    Icons.TwoTone.ShoppingCart to "Shopping",
    Icons.TwoTone.LocalAtm to "ATM",
    Icons.TwoTone.AccountBalanceWallet to "Wallet",
    Icons.TwoTone.MonetizationOn to "Finance",
    Icons.TwoTone.Paid to "Payment",
    Icons.TwoTone.Savings to "Savings",
    Icons.TwoTone.Restaurant to "Dining",
    Icons.TwoTone.LocalGroceryStore to "Groceries",
    Icons.TwoTone.DirectionsCar to "Transportation",
    Icons.TwoTone.Home to "Housing",
    Icons.TwoTone.Work to "Income",
    Icons.TwoTone.Flight to "Travel",
    Icons.TwoTone.HealthAndSafety to "Health",
    Icons.TwoTone.FitnessCenter to "Fitness",
    Icons.TwoTone.School to "Education",
    Icons.TwoTone.Pets to "Pets",
    Icons.TwoTone.ChildCare to "Childcare",
    Icons.TwoTone.TheaterComedy to "Entertainment",
    Icons.TwoTone.MusicNote to "Music",
    Icons.TwoTone.SportsEsports to "Gaming",
    Icons.TwoTone.Spa to "Self Care",
    Icons.TwoTone.ElectricBolt to "Utilities"
).map { (icon, name) -> IconItem(icon, name) }

val mealIcons = listOf(
    Icons.TwoTone.EmojiFoodBeverage to "Food",
    Icons.TwoTone.Restaurant to "Dining",
    Icons.TwoTone.LocalGroceryStore to "Groceries",
    Icons.TwoTone.LocalDining to "Restaurant",
    Icons.TwoTone.LocalCafe to "Coffee/Tea",
    Icons.TwoTone.LunchDining to "Meals",
    Icons.TwoTone.Cake to "Desserts",
    Icons.TwoTone.LocalPizza to "Fast Food",
    Icons.TwoTone.SetMeal to "Meal Deals",
    Icons.TwoTone.RamenDining to "Noodles",
    Icons.TwoTone.BakeryDining to "Bakery",
    Icons.TwoTone.Nightlife to "Alcohol",
    Icons.TwoTone.Icecream to "Snacks",
    Icons.TwoTone.Fastfood to "Takeout",
    Icons.TwoTone.Coffee to "Coffee",
    Icons.TwoTone.WineBar to "Wine",
    Icons.TwoTone.BreakfastDining to "Breakfast",
    Icons.TwoTone.Cookie to "Sweets",
    Icons.TwoTone.SoupKitchen to "Soup",
    Icons.TwoTone.RiceBowl to "Grains",
    Icons.TwoTone.Grass to "Vegetarian",
).map { (icon, name) -> IconItem(icon, name) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseIconBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    isForFood: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onIconClick: (IconItem) -> Unit = {}
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
            (if (isForFood) mealIcons else expenseIcons)
                .forEach {
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier.clickable {
                            onIconClick(it)
                            onDismissRequest()
                        },
                        headlineContent = {
                            Text(it.displayName)
                        },
                        trailingContent = {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.displayName
                            )
                        }
                    )
                }
        }
    }
}

