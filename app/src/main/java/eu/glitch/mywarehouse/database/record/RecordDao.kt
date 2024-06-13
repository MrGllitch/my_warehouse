package eu.glitch.mywarehouse.database.record

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Query("SELECT * FROM records")
    fun getAllRecords(): Flow<List<Record>>

    @Insert
    suspend fun insertRecord(record: Record): Long

    @Delete
    suspend fun deleteRecord(record: Record)

    @Update
    suspend fun updateRecord(record: Record): Int

    @Query("SELECT * FROM records WHERE r_id = :id")
    suspend fun getRecord(id: Long): Record

    @Query(
        "SELECT * FROM records r " +
                "JOIN products p ON r.product_id = p.p_id " +
                "JOIN types t ON r.type_id = t.t_id " +
                "JOIN units u ON r.unit_id = u.u_id "
    )
    fun getRecordWithDetails(): Flow<List<RecordDetails>>
    //+"ORDER BY r.date_to ASC"

}