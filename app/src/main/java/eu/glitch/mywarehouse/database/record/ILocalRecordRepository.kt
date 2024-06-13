package eu.glitch.mywarehouse.database.record

import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordDetails
import kotlinx.coroutines.flow.Flow


interface ILocalRecordRepository {
    fun getRecords(): Flow<List<Record>>

    suspend fun insertRecord(record: Record): Long

    suspend fun updateRecord(record: Record)

    suspend fun deleteRecord(record: Record)

    suspend fun getRecord(id: Long): Record

    fun getRecordWithDetails(): Flow<List<RecordDetails>>


}