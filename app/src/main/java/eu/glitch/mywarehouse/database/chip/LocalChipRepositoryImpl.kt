package eu.glitch.mywarehouse.database.chip

import eu.glitch.mywarehouse.model.Chip
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalChipRepositoryImpl @Inject constructor(private val chipDao: ChipDao) :
    ILocalChipRepository {
    override fun getChips(): Flow<List<Chip>> {
        return chipDao.getAllChips()
    }

    override suspend fun insertChip(chip: Chip): Long {
        return chipDao.insertChip(chip)
    }

    override suspend fun updateChip(chip: Chip) {
        chipDao.updateChip(chip = chip)
    }

    override suspend fun deleteChip(chip: Chip) {
        chipDao.deleteChip(chip = chip)
    }

    override suspend fun getChip(id: Long): Chip {
        return chipDao.getChip(id = id)
    }


}