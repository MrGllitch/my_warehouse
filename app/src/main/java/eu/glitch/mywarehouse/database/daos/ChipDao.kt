package eu.glitch.mywarehouse.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Chip

@Dao
interface ChipDao {
    @Query("SELECT * FROM chips")
    fun getAllChips(): List<Chip>

    @Insert
    suspend fun insertChip(chip: Chip)

    @Delete
    suspend fun deleteChip(chip: Chip)

    @Update
    suspend fun updateChip(chip: Chip)

    @Query("SELECT * FROM chips WHERE ch_id = :id")
    suspend fun getChip(id: String): Chip
}