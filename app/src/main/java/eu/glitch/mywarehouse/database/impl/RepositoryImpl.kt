package eu.glitch.mywarehouse.database.impl

import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.model.Chip
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.ProductChipCrossRef
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordListItem
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val account: AccountServiceImpl, private val cloud: FirestoreRepositoryImpl, private val local: LocalRepositoryImpl) :
    IRepository {
    private val currentDataSource: IRepository
        get() = if (account.hasUser)  cloud else local


    suspend fun isAdmin(): Boolean {
        if (account.hasUser) {
            return cloud.fetchCurrentUserRole() == "admin"
        }
        return true
    }

    suspend fun syncDataToCloud(): Flow<SyncStatus> = flow {
        try {
            emit(SyncStatus.Syncing)

            emit(SyncStatus.TableSyncing("Products"))
            syncTable(local.getProducts(), cloud::insertProduct)

            emit(SyncStatus.TableSyncing("Records"))
            syncTable(local.getRecords(), cloud::insertRecord)

            emit(SyncStatus.TableSyncing("Chips"))
            syncTable(local.getChips(), cloud::insertChip)

            emit(SyncStatus.TableSyncing("Types"))
            syncTable(local.getTypes(), cloud::insertType)

            emit(SyncStatus.TableSyncing("Units"))
            syncTable(local.getUnits(), cloud::insertUnit)

            emit(SyncStatus.Success)
        } catch (e: Exception) {
            emit(SyncStatus.Error(e.message ?: "Unknown error"))
        }
    }

    private suspend fun <T> syncTable(data: List<T>, saveFunction: suspend (T) -> Unit) {
        data.forEach { item ->
            saveFunction(item)
        }
    }

    override suspend fun isInitialized(): Boolean {
        return if (currentDataSource is FirestoreRepositoryImpl) {
            cloud.isInitialized()
        }else{
            true
        }
    }

    override suspend fun getProducts(): List<Product> {
        return currentDataSource.getProducts()
    }

    override suspend fun insertProduct(product: Product) : String {
        return currentDataSource.insertProduct(product)
    }

    override suspend fun updateProduct(product: Product) {
        currentDataSource.updateProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        currentDataSource.deleteProduct(product)
    }

    override suspend fun getProduct(id: String): Product {
        return currentDataSource.getProduct(id)
    }

    override suspend fun insertProductChipCrossRef(crossRef: ProductChipCrossRef) {
        currentDataSource.insertProductChipCrossRef(crossRef)
    }

    override suspend fun deleteProductChipCrossRef(crossRef: ProductChipCrossRef) {
        currentDataSource.deleteProductChipCrossRef(crossRef)
    }

    override suspend fun updateProductWithChips(product: Product, newChipIds: List<String>) {
        currentDataSource.updateProductWithChips(product, newChipIds)
    }

    override suspend fun getChipIdsForProduct(productId: String): List<String> {
        return currentDataSource.getChipIdsForProduct(productId)
    }

    override suspend fun getRecords(): List<Record> {
        return currentDataSource.getRecords()
    }

    override suspend fun insertRecord(record: Record) {
        currentDataSource.insertRecord(record)
    }

    override suspend fun updateRecord(record: Record) {
        currentDataSource.updateRecord(record)
    }

    override suspend fun deleteRecord(record: Record) {
        currentDataSource.deleteRecord(record)
    }

    override suspend fun getRecord(id: String): Record {
        return currentDataSource.getRecord(id)
    }

    override suspend fun getRecordListItems(): List<RecordListItem> {
        return currentDataSource.getRecordListItems()
    }

    override suspend fun insertUnit(unit: WarehouseUnit) {
        currentDataSource.insertUnit(unit)
    }

    override suspend fun getUnits(): List<WarehouseUnit> {
        return currentDataSource.getUnits()
    }

    override suspend fun getUnitsSize(): Int {
        return currentDataSource.getUnitsSize()
    }

    override suspend fun getUnit(id: String): WarehouseUnit {
        return currentDataSource.getUnit(id)
    }

    override suspend fun updateUnit(unit: WarehouseUnit) {
        currentDataSource.updateUnit(unit)
    }

    override suspend fun deleteUnit(unit: WarehouseUnit) {
        currentDataSource.deleteUnit(unit)
    }

    override suspend fun getTypes(): List<Type> {
        return currentDataSource.getTypes()
    }

    override suspend fun insertType(type: Type) {
        currentDataSource.insertType(type)
    }

    override suspend fun deleteType(type: Type) {
        currentDataSource.deleteType(type)
    }

    override suspend fun updateType(type: Type) {
        currentDataSource.updateType(type)
    }

    override suspend fun getType(id: String): Type {
        return currentDataSource.getType(id)
    }

    override suspend fun getChips(): List<Chip> {
        return currentDataSource.getChips()
    }

    override suspend fun insertChip(chip: Chip) : String{
        return currentDataSource.insertChip(chip)
    }

    override suspend fun updateChip(chip: Chip) {
        currentDataSource.updateChip(chip)
    }

    override suspend fun deleteChip(chip: Chip) {
        currentDataSource.deleteChip(chip)
    }

    override suspend fun getChip(id: String): Chip {
        return currentDataSource.getChip(id)
    }
}

sealed class SyncStatus {
    object Waiting: SyncStatus()
    object Syncing : SyncStatus()
    data class TableSyncing(val tableName: String) : SyncStatus()
    object Success : SyncStatus()
    data class Error(val message: String) : SyncStatus()
}