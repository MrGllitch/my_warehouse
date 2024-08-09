package eu.glitch.mywarehouse.database

import eu.glitch.mywarehouse.model.WarehouseUnit
import javax.inject.Inject

class Repository @Inject constructor(private val account: AccountServiceImpl, private val cloud: FirestoreRepositoryImpl, private val local: LocalRepositoryImpl) : IRepository {
    private val currentDataSource: IRepository
        get() = if (account.hasPremium)  cloud else local

    override suspend fun insertUnit(unit: WarehouseUnit) {
        currentDataSource.insertUnit(unit)
    }

    override suspend fun getUnits(): List<WarehouseUnit> {
        return currentDataSource.getUnits()
    }

}