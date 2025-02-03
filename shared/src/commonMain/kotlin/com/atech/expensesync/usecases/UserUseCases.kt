package com.atech.expensesync.usecases

import com.atech.expensesync.database.ktor.ExpenseSyncClient
import com.atech.expensesync.database.models.DesktopLogInDetails
import com.atech.expensesync.database.models.User
import com.atech.expensesync.utils.ResponseDataState

data class UserUseCases(
    val createUserUseCase: CreateUserUseCase,
    val logInToDesktopUseCase: LogInToDesktopUseCase
)


data class CreateUserUseCase(
    private val client: ExpenseSyncClient
) {
    suspend operator fun invoke(
        model: User
    ) = client.logInUser(model)
}

data class LogInToDesktopUseCase(
    private val client: ExpenseSyncClient
) {
    suspend operator fun invoke(
        uid: String,
        model: DesktopLogInDetails
    ): ResponseDataState<String> = client.logInToDesktop(uid, model)
}