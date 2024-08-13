package eu.glitch.mywarehouse.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Type

@Dao
interface TypeDao {
    @Query("SELECT * FROM types")
    fun getAllTypes(): List<Type>

    @Insert
    suspend fun insertType(type: Type)

    @Delete
    suspend fun deleteType(type: Type)

    @Update
    suspend fun updateType(type: Type)

    @Query("SELECT * FROM types WHERE t_id = :id")
    suspend fun getType(id: String): Type
}