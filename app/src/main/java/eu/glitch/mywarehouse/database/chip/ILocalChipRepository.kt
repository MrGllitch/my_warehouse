package eu.glitch.mywarehouse.database.chip

import eu.glitch.mywarehouse.model.Chip
import kotlinx.coroutines.flow.Flow


interface ILocalChipRepository {
    fun getChips(): Flow<List<Chip>>
    suspend fun insertChip(chip: Chip): Long
    suspend fun updateChip(chip: Chip)
    suspend fun deleteChip(chip: Chip)
    suspend fun getChip(id: Long): Chip
}