package eu.glitch.mywarehouse.database.record

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordListItemBase
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
        """
SELECT r.*, 
       p.name AS name,
       u.shortcut AS shortcut, 
       t.t_id AS typeId,
       t.iconRoute AS iconRoute
FROM records r
INNER JOIN products p ON r.product_id = p.p_id
LEFT JOIN units u ON p.unit_id = u.u_id
LEFT JOIN types t ON p.type_id = t.t_id
    """
    )
    fun getRecordListItems(): Flow<List<RecordListItemBase>>

    @Query("SELECT ch_id FROM product_chips WHERE p_id = :productId")
    suspend fun getChipIdsForProduct(productId: Long): List<Long>

}