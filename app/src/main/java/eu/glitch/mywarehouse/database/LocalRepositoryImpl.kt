package eu.glitch.mywarehouse.database

import eu.glitch.mywarehouse.database.unit.UnitDao
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val unitDao: UnitDao) : IRepository {
    override suspend fun insertUnit(unit: WarehouseUnit) {
        unitDao.insertUnit(unit)
    }

    override suspend fun getUnits(): List<WarehouseUnit> {
        return withContext(Dispatchers.IO) {
            unitDao.getAllUnits()
        }
    }
}