package eu.glitch.mywarehouse.database.record

import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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

    override fun getRecordListItems(): Flow<List<RecordListItem>> = flow {

        val baseList = recordDao.getRecordListItems().first()
        val recordListItems = baseList.map { base ->
            val chipIds = recordDao.getChipIdsForProduct(base.record.productId)
            RecordListItem(
                record = base.record,
                name = base.name,
                shortcut = base.shortcut,
                typeId = base.typeId,
                chipIds = chipIds,
                iconRoute = base.iconRoute
            )
        }
        emit(recordListItems)

        // return recordDao.getRecordListItems()
    }


}