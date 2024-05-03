package eu.glitch.mywarehouse.database.unit

import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.Flow


interface ILocalUnitRepository {
    fun getUnits(): Flow<WarehouseUnit>

    suspend fun insertUnit(unit: WarehouseUnit) : Long

}