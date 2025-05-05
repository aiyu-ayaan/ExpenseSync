package com.atech.expensesync.firebase.usecase

import com.atech.expensesync.database.models.SplitFirebase
import com.atech.expensesync.database.models.SplitTransaction
import com.atech.expensesync.database.models.TransactionGlobalModel
import com.atech.expensesync.database.models.toTransactionGlobalModel
import com.atech.expensesync.firebase.io.KmpFire
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.firebase.util.getException
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isError
import com.atech.expensesync.firebase.util.isSuccess
import com.atech.expensesync.utils.FirebaseCollectionPath
import com.atech.expensesync.utils.FirebaseDocumentName
import kotlinx.coroutines.flow.Flow


data class SplitUseCases(
    val createSplitGroup: CreateSplitGroup,
    val getSplitData: GetSplitData,
    val getSplitById: GetSplitById,
    val createTransaction: CreateTransaction,
    val getGlobalTransaction: GetGlobalTransaction,
    val getTransaction: GetTransaction
)

data class CreateSplitGroup(
    private val kmpFire: KmpFire
) {
    suspend operator fun invoke(
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
    suspend operator fun invoke(
        uid: String
    ): Flow<FirebaseResponse<List<SplitFirebase>>> =
        kmpFire.getObservedDataWithArrayContains<SplitFirebase>(
            collectionName = FirebaseCollectionPath.SPLIT.path, query = Pair("groupMembers", uid)
        )
}

data class GetSplitById(
    private val kmpFire: KmpFire
) {
    suspend operator fun invoke(
        groupId: String
    ): Flow<FirebaseResponse<SplitFirebase>> = kmpFire.getObservedData<SplitFirebase>(
        collectionName = FirebaseCollectionPath.SPLIT.path, documentName = groupId
    )
}

data class CreateTransaction(
    private val kmpFire: KmpFire
) {
    suspend operator fun invoke(
        groupId: String, splitDetails: SplitTransaction
    ): Exception? {
        val elementInsert = kmpFire.insertData(
            collectionName = FirebaseCollectionPath.SPLIT.path + "/$groupId/${FirebaseDocumentName.SPLIT_TRANSACTION_ENTRY.path}",
            documentName = splitDetails.id,
            data = splitDetails
        )
        val globalTransactionCollectionName =
            FirebaseCollectionPath.SPLIT.path + "/$groupId/${FirebaseDocumentName.SPLIT_TRANSACTION.path}"
        if (elementInsert.isSuccess()) {
            splitDetails.toTransactionGlobalModel(splitDetails.createdByUid).filter {
                it.path != splitDetails.createdByUid
            }.forEach {
                val temp = kmpFire.getData<TransactionGlobalModel>(
                    collectionName = globalTransactionCollectionName, documentName = it.path
                )
                val editedGlobalTransaction: TransactionGlobalModel =
                    if (temp.isSuccess()) temp.getOrNull()?.let { nonNullableData ->
                        nonNullableData.copy(
                            totalOwe = nonNullableData.totalOwe + it.totalOwe
                        )
                    } ?: it
                    else it

                val log = kmpFire.insertData(
                    collectionName = globalTransactionCollectionName,
                    documentName = it.path,
                    data = editedGlobalTransaction
                )
                if (log.isError()) {
                    return log.getException()
                }
            }
        }
        if (elementInsert.isError()) {
            return elementInsert.getException()
        }
        return null
    }
}


data class GetGlobalTransaction(
    private val kmpFire: KmpFire
) {
    suspend operator fun invoke(
        groupId: String
    ) = kmpFire.getObservedCollection<TransactionGlobalModel>(
        collectionName = FirebaseCollectionPath.SPLIT.path + "/$groupId/${FirebaseDocumentName.SPLIT_TRANSACTION.path}"
    )
}

data class GetTransaction(
    private val kmpFire: KmpFire
) {
    suspend operator fun invoke(
        groupId: String
    ) = kmpFire.getObservedCollection<SplitTransaction>(
        collectionName = FirebaseCollectionPath.SPLIT.path + "/$groupId/${FirebaseDocumentName.SPLIT_TRANSACTION_ENTRY.path}"
    )
}
