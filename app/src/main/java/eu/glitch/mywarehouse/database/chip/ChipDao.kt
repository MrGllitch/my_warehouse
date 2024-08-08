package eu.glitch.mywarehouse.database.chip

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Chip
import kotlinx.coroutines.flow.Flow

@Dao
interface ChipDao {
    @Query("SELECT * FROM chips")
    fun getAllChips(): Flow<List<Chip>>

    @Insert
    suspend fun insertChip(chip: Chip): Long

    @Delete
    suspend fun deleteChip(chip: Chip)

    @Update
    suspend fun updateChip(chip: Chip): Int

    @Query("SELECT * FROM chips WHERE ch_id = :id")
    suspend fun getChip(id: Long): Chip
}