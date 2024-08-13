package eu.glitch.mywarehouse.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordListItemBase

@Dao
interface RecordDao {
    @Query("SELECT * FROM records")
    fun getAllRecords(): List<Record>

    @Insert
    suspend fun insertRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

    @Update
    suspend fun updateRecord(record: Record)

    @Query("SELECT * FROM records WHERE r_id = :id")
    suspend fun getRecord(id: String): Record

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
    fun getRecordListItems(): List<RecordListItemBase>

    @Query("SELECT ch_id FROM product_chips WHERE p_id = :productId")
    suspend fun getChipIdsForProduct(productId: String): List<String>

}