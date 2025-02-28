package com.atech.expensesync.ui.screens.meal.root

data class AddMealBookState(
    val name: String = "",
    val defaultPrice: Double = 0.0,
    val description: String = "",
    val defaultCurrency: String = "INR",
)