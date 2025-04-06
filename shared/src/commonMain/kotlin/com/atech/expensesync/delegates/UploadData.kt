package com.atech.expensesync.delegates

import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.room.upload.UpdateType
import com.atech.expensesync.firebase.usecase.MealBookUploadUseCase
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.usecases.UploadUseCases
import com.atech.expensesync.utils.LoggerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface UploadData {


    fun setVariables(
        mealBookUploadUseCase: MealBookUploadUseCase,
        uploadUseCase: UploadUseCases,
        prefManager: PrefManager,
        scope: CoroutineScope
    )

    fun uploadMealData()
}


class UploadDataDelegate : UploadData {
    private var mealBookUploadUseCase: MealBookUploadUseCase? = null
    private var prefManager: PrefManager? = null
    private var scope: CoroutineScope? = null
    private var uploadUseCase: UploadUseCases? = null
    private lateinit var userUid: String


    override fun setVariables(
        mealBookUploadUseCase: MealBookUploadUseCase,
        uploadUseCase: UploadUseCases,
        prefManager: PrefManager,
        scope: CoroutineScope
    ) {
        this.mealBookUploadUseCase = mealBookUploadUseCase
        this.prefManager = prefManager
        this.scope = scope
        this.uploadUseCase = uploadUseCase
        userUid = prefManager.getString(PrefKeys.USER_ID)
    }

    override fun uploadMealData() {
        if (mealBookUploadUseCase == null || prefManager == null || scope == null || uploadUseCase == null) {
            com.atech.expensesync.utils.expenseSyncLogger(
                "MealBookUploadUseCase or UploadUseCases or PrefManager or Scope is null",
                LoggerType.ERROR
            )
            return
        }
        userUid = prefManager!!.getString(PrefKeys.USER_ID)
        if (userUid.isBlank()) {
            return
        }

        scope!!.launch(Dispatchers.IO) {
            val item = uploadUseCase!!.getAllUnUploadByType.invoke(UpdateType.MEAL)
            if (item != null) when (val d = mealBookUploadUseCase!!.invoke(
                userUid
            )) {
                is FirebaseResponse.Error -> {
                    com.atech.expensesync.utils.expenseSyncLogger(
                        "Error in uploading meal data: ${d.error}", LoggerType.ERROR
                    )
                }

                FirebaseResponse.Loading -> {}
                is FirebaseResponse.Success<*> -> {
                    uploadUseCase!!.update.invoke(
                        item.copy(isUpdated = true)
                    )
                    com.atech.expensesync.utils.expenseSyncLogger(
                        "Data is uploaded successfully!!"
                    )
                }
            }
        }

    }

    companion object {
        fun lazy() = object : ReadOnlyProperty<Any?, UploadDataDelegate> {
            private var instance: UploadDataDelegate? = null

            override fun getValue(thisRef: Any?, property: KProperty<*>): UploadDataDelegate {
                return instance ?: UploadDataDelegate().also { instance = it }
            }
        }
    }

}