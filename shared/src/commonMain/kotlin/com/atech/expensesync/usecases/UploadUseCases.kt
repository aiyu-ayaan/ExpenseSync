package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.upload.UpdateType
import com.atech.expensesync.database.room.upload.UploadDao
import com.atech.expensesync.database.room.upload.UploadModel

data class UploadUseCases(
    val getAll: GetAllUseCases,
    val inset: InsertUploadUseCases,
    val update: UpdateUploadUseCases,
    val getAllUnUploadByType: GetAllUnUploadByTypeUseCases,
    val getAllUnUpload: GetAllUnUploadUseCases
)


data class InsertUploadUseCases(
    private val dao: UploadDao
) {
    suspend operator fun invoke(
        uploadModule: UploadModel
    ) {
        val lastUploaded = dao.getUnUploadModelByType(uploadModule.updatedType.name)
        if (lastUploaded != null) dao.updateUploadModel(
            lastUploaded.copy(
                isUpdated = false, updatedTime = System.currentTimeMillis()
            )
        )
        else dao.insertUploadModel(
            uploadModule
        )
    }
}

data class UpdateUploadUseCases(
    private val dao: UploadDao
) {
    suspend operator fun invoke(
        uploadModule: UploadModel
    ) {
        dao.updateUploadModel(
            uploadModule
        )
    }
}

data class GetAllUnUploadByTypeUseCases(
    private val dao: UploadDao
) {
    suspend operator fun invoke(
        type: UpdateType
    ) = dao.getUnUploadModelByType(type.name)

}

data class GetAllUnUploadUseCases(
    private val dao: UploadDao
) {
    suspend operator fun invoke() = dao.getAllUnUploadModel()
}

data class GetAllUseCases(
    private val dao: UploadDao
) {
    suspend operator fun invoke() = dao.getAll()
}
