package eu.glitch.mywarehouse.database

import eu.glitch.mywarehouse.model.WarehouseUnit

interface IRepository {
    //units
    suspend fun insertUnit(unit: WarehouseUnit)
    suspend fun getUnits(): List<WarehouseUnit>

}