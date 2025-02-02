package com.atech.expensesync.usecases

import com.atech.expensesync.database.ktor.ExpenseSyncClient
import com.atech.expensesync.database.models.User

data class UserUseCases(
    val createUserUseCase: CreateUserUseCase
)


data class CreateUserUseCase(
    private val client: ExpenseSyncClient
) {
    suspend operator fun invoke(
        model: User
    ) = client.logInUser(model)
}