package eu.glitch.mywarehouse.database.record

import eu.glitch.mywarehouse.model.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRecordRepositoryImpl @Inject constructor(private val recordDao: RecordDao) :
    ILocalRecordRepository {


    override fun getRecords(): Flow<Record> {
        return recordDao.getAllRecords()
    }

    override suspend fun insertRecord(record: Record): Long {
        return recordDao.insertRecord(record = record)
    }


}