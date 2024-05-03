package eu.glitch.mywarehouse.database.type

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
interface TypeDao {
    @Query("SELECT * FROM types")
    fun getAllTypes(): Flow<Type>

    @Insert
    suspend fun insertType(type: Type) : Long

    @Delete
    suspend fun deleteType(type: Type)

    @Update
    suspend fun updateType(type: Type) : Int

}