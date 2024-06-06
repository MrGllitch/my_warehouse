package eu.glitch.mywarehouse.database.record

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.WarehouseUnit
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

    @Query("SELECT * FROM records WHERE id = :id")
    suspend fun getRecord(id: Long): Record

    @Query("SELECT * FROM units")
    fun getAllUnits(): Flow<List<WarehouseUnit>>

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

}