package com.atech.expensesync.utils

interface EntityMapper<T, V> {
    fun mapFromEntity(entity: T): V
    fun mapToEntity(domainModel: V): T

    fun mapFromEntityList(entities: List<T>): List<V> =
        entities.map { mapFromEntity(it) }

    fun mapToEntityList(domainModels: List<V>): List<T> =
        domainModels.map { mapToEntity(it) }

}