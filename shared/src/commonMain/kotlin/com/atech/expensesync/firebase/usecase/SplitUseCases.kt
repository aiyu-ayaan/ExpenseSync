package com.atech.expensesync.firebase.usecase

import com.atech.expensesync.database.models.SplitFirebase
import com.atech.expensesync.firebase.io.KmpFire
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.utils.FirebaseCollectionPath
import kotlinx.coroutines.flow.Flow


data class SplitUseCases(
    val createSplitGroup: CreateSplitGroup,
    val getSplitData: GetSplitData
)

data class CreateSplitGroup(
    private val kmpFire: KmpFire
) {
    suspend fun invoke(
        model: SplitFirebase
    ): FirebaseResponse<SplitFirebase> {
        return kmpFire.insertData(
            collectionName = FirebaseCollectionPath.SPLIT.path,
            documentName = model.groupID,
            data = model
        )
    }
}

data class GetSplitData(
    private val kmpFire: KmpFire
) {
    suspend fun invoke(
        uid: String
    ): Flow<FirebaseResponse<List<SplitFirebase>>> =
        kmpFire.getObservedDataWithArrayContains<SplitFirebase>(
            collectionName = FirebaseCollectionPath.SPLIT.path, query = Pair("groupMembers", uid)
        )


}

