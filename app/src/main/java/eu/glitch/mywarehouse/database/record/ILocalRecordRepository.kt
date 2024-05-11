package eu.glitch.mywarehouse.database.record

import eu.glitch.mywarehouse.model.Record
import kotlinx.coroutines.flow.Flow


interface ILocalRecordRepository {
    fun getRecords(): Flow<List<Record>>

    suspend fun insertRecord(record: Record): Long

}