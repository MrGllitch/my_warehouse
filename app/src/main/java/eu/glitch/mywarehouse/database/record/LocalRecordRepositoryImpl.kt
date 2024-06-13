package eu.glitch.mywarehouse.database.record

import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRecordRepositoryImpl @Inject constructor(private val recordDao: RecordDao) :
    ILocalRecordRepository {


    override fun getRecords(): Flow<List<Record>> {
        return recordDao.getAllRecords()
    }

    override suspend fun insertRecord(record: Record): Long {
        return recordDao.insertRecord(record = record)
    }

    override suspend fun updateRecord(record: Record) {
        recordDao.updateRecord(record)
    }

    override suspend fun deleteRecord(record: Record) {
        recordDao.deleteRecord(record)
    }

    override suspend fun getRecord(id: Long): Record {
        return recordDao.getRecord(id)
    }

    override fun getRecordWithDetails(): Flow<List<RecordDetails>> {
        return recordDao.getRecordWithDetails()
    }


}