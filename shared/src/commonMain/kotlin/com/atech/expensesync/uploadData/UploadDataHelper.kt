package com.atech.expensesync.uploadData

import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.room.upload.UpdateType
import com.atech.expensesync.firebase.usecase.MealBookUploadUseCase
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.utils.LoggerType
import com.atech.expensesync.utils.expenseSyncLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UploadDataHelper(
    private val mealBookUploadUseCase: MealBookUploadUseCase,
    val prefManager: PrefManager,
    val scope: CoroutineScope,
    private val uploadUseCase: UploadUseCases
) {
    fun uploadMealData() {
        val userUid = prefManager.getString(PrefKeys.USER_ID)
        if (userUid.isBlank()) {
            return
        }
        scope.launch(Dispatchers.IO) {
            val item = uploadUseCase.getAllUnUploadByType.invoke(UpdateType.MEAL)
            if (item != null) when (val d = mealBookUploadUseCase.invoke(
                userUid
            )) {
                is FirebaseResponse.Error -> {
                    expenseSyncLogger(
                        "Error in uploading meal data: ${d.error}", LoggerType.ERROR
                    )
                }

                FirebaseResponse.Loading -> {}
                is FirebaseResponse.Success<*> -> {
                    uploadUseCase.update.invoke(
                        item.copy(isUpdated = true)
                    )
                    expenseSyncLogger(
                        "Data is uploaded successfully!!"
                    )
                }

                FirebaseResponse.Empty -> {}
            }
        }

    }
}