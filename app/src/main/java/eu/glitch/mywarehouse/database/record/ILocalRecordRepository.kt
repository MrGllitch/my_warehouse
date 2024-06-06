package eu.glitch.mywarehouse.database.record

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.Flow


interface ILocalRecordRepository {
    fun getRecords(): Flow<List<Record>>

    suspend fun insertRecord(record: Record): Long

    suspend fun updateRecord(record: Record)

    suspend fun deleteRecord(record: Record)

    suspend fun getRecord(id: Long): Record

    fun getUnits(): Flow<List<WarehouseUnit>>

    fun getProducts(): Flow<List<Product>>


}