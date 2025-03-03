package com.atech.expensesync.ui.screens.meal.root

import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.EntityMapper
import java.util.UUID

data class AddMealBookState(
    val name: String = "",
    val defaultPrice: Double = 0.0,
    val description: String = "",
    val defaultCurrency: Currency = Currency.INR,
    val mealBookId: String = UUID.randomUUID().toString(),
)


class AddMealBookStateTOMealBookMapper : EntityMapper<AddMealBookState, MealBook> {
    override fun mapFromEntity(entity: AddMealBookState): MealBook =
        MealBook(
            name = entity.name,
            defaultPrice = entity.defaultPrice,
            description = entity.description,
            defaultCurrency = entity.defaultCurrency,
            mealBookId = entity.mealBookId
        )

    override fun mapToEntity(domainModel: MealBook): AddMealBookState =
        AddMealBookState(
            name = domainModel.name,
            defaultPrice = domainModel.defaultPrice,
            description = domainModel.description,
            defaultCurrency = domainModel.defaultCurrency,
            mealBookId = domainModel.mealBookId
        )

}