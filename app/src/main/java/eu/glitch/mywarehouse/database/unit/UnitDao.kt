package eu.glitch.mywarehouse.database.unit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Query("SELECT * FROM units")
    fun getAllUnits(): Flow<List<WarehouseUnit>>

    @Query("SELECT COUNT(*) From units")
    suspend fun getUnitsSize(): Int

    @Insert
    suspend fun insertUnit(unit: WarehouseUnit): Long

    @Delete
    suspend fun deleteUnit(unit: WarehouseUnit)

    @Update
    suspend fun updateUnit(unit: WarehouseUnit): Int

    @Query("SELECT * FROM units WHERE u_id = :id")
    suspend fun getUnit(id: Long): WarehouseUnit
}