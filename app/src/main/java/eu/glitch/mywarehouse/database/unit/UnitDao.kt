package eu.glitch.mywarehouse.database.unit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Query("SELECT * FROM units")
    fun getAllUnits(): Flow<WarehouseUnit>

    @Insert
    suspend fun insertUnit(unit: WarehouseUnit) : Long

    @Delete
    suspend fun deleteUnit(unit: WarehouseUnit)

    @Update
    suspend fun updateUnit(unit: WarehouseUnit) : Int
}