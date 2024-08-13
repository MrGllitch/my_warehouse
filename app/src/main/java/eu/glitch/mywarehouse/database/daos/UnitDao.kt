package eu.glitch.mywarehouse.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.WarehouseUnit

@Dao
interface UnitDao {
    @Query("SELECT * FROM units")
    fun getAllUnits(): List<WarehouseUnit>

    @Query("SELECT COUNT(*) From units")
    suspend fun getUnitsSize(): Int

    @Insert
    suspend fun insertUnit(unit: WarehouseUnit)

    @Delete
    suspend fun deleteUnit(unit: WarehouseUnit)

    @Update
    suspend fun updateUnit(unit: WarehouseUnit)

    @Query("SELECT * FROM units WHERE u_id = :id")
    suspend fun getUnit(id: String): WarehouseUnit
}