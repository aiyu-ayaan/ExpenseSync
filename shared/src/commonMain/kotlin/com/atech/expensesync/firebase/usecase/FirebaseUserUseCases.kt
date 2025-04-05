package com.atech.expensesync.firebase.usecase

import com.atech.expensesync.database.models.User
import com.atech.expensesync.firebase.io.KmpFire
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.utils.FirebaseCollectionPath


data class FirebaseUserUseCases(
    val createUser: CreateUserUseCase,
)

data class CreateUserUseCase(
    private val kmpFire: KmpFire
) {
    suspend operator fun invoke(user: User): FirebaseResponse<User> {
        val data = kmpFire.getData<User>(
            FirebaseCollectionPath.USER.path,
            user.uid
        )
        return if (data is FirebaseResponse.Success) {
            FirebaseResponse.Success(data.data)
        } else {
            val result = kmpFire.insertData(
                FirebaseCollectionPath.USER.path,
                user.uid,
                user
            )
            if (result is FirebaseResponse.Success) {
                FirebaseResponse.Success(result.data)
            } else {
                FirebaseResponse.Error("Failed to create user")
            }
        }
    }
}
