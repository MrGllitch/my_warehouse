package eu.glitch.mywarehouse.database.unit

import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalUnitRepositoryImpl @Inject constructor(private val unitDao: UnitDao) :
    ILocalUnitRepository {
    override fun getUnits(): Flow<List<WarehouseUnit>> {
        return unitDao.getAllUnits()
    }

    override suspend fun insertUnit(unit: WarehouseUnit): Long {
        return unitDao.insertUnit(unit = unit)
    }

    override suspend fun getUnit(id: Long): WarehouseUnit {
        return unitDao.getUnit(id)
    }

    override suspend fun updateUnit(unit: WarehouseUnit) {
        unitDao.updateUnit(unit = unit)
    }

    override suspend fun deleteUnit(unit: WarehouseUnit) {
        unitDao.deleteUnit(unit)
    }


}