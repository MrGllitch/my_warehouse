package eu.glitch.mywarehouse.database.unit

import eu.glitch.mywarehouse.database.type.TypeDao
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalUnitRepositoryImpl @Inject constructor(private val unitDao: UnitDao) :
    ILocalUnitRepository {
    override fun getUnits(): Flow<WarehouseUnit> {
        return unitDao.getAllUnits()
    }

    override suspend fun insertUnit(unit: WarehouseUnit): Long {
        return unitDao.insertUnit(unit = unit)
    }


}