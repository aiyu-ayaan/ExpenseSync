package com.atech.expensesync.firebase.usecase

import com.atech.expensesync.database.models.DesktopLogInDetails
import com.atech.expensesync.database.models.User
import com.atech.expensesync.firebase.helper.FirebaseHelper
import com.atech.expensesync.firebase.io.KmpFire
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.utils.FirebaseCollectionPath
import kotlinx.coroutines.flow.Flow


data class FirebaseUserUseCases(
    val createUser: CreateUserUseCase,
    val logInToDesktopUseCase: LogInToDesktopUseCase,
    val observeLogInUsingOR: ObserveLogInUsingOR,
    val getLogInDetails: GetLogInDetails
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

data class LogInToDesktopUseCase(
    private val kmpFire: KmpFire
) {
    suspend operator fun invoke(
        uid: String, model: DesktopLogInDetails
    ): FirebaseResponse<DesktopLogInDetails> =
        kmpFire.updateDataMap(
            FirebaseCollectionPath.USER.path,
            uid,
            mapOf(
                "systemUid" to model.systemUid,
                "desktopLogInDetails" to model
            )
        )

}

data class ObserveLogInUsingOR(
    private val kmpFire: KmpFire
) {
    suspend operator fun invoke(
        desktopId: String,
    ): Flow<FirebaseResponse<User>> =
        kmpFire.getObservedDataWithQuery<User>(
            FirebaseHelper(
                collectionName = FirebaseCollectionPath.USER.path,
                queries = "systemUid" to desktopId
            )
        )
}

data class GetLogInDetails(
    private val kmpFire: KmpFire
) {
    suspend operator fun invoke(
        uid: String,
    ): Flow<FirebaseResponse<User>> =
        kmpFire.getObservedData(
            FirebaseCollectionPath.USER.path,
            uid
        )
}
