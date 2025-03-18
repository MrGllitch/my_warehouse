package eu.glitch.mywarehouse.database.impl

import eu.glitch.mywarehouse.database.daos.ChipDao
import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.database.daos.ProductDao
import eu.glitch.mywarehouse.database.daos.RecordDao
import eu.glitch.mywarehouse.database.daos.TypeDao
import eu.glitch.mywarehouse.database.daos.UnitDao
import eu.glitch.mywarehouse.model.Chip
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.ProductChipCrossRef
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordListItem
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val unitDao: UnitDao, private val productDao: ProductDao, private val chipDao: ChipDao, private val recordDao: RecordDao, private val typeDao: TypeDao) :
    IRepository {
    override suspend fun getProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            productDao.getAllProducts()
        }
    }

    override suspend fun insertProduct(product: Product) : String {
        val newId = genId()
        productDao.insertProduct(product.copy(id = newId))
        return newId
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    override suspend fun getProduct(id: String): Product {
        return productDao.getProduct(id)
    }

    override suspend fun insertProductChipCrossRef(crossRef: ProductChipCrossRef) {
        productDao.insertProductChipCrossRef(crossRef)
    }

    override suspend fun deleteProductChipCrossRef(crossRef: ProductChipCrossRef) {
        productDao.deleteProductChipCrossRef(crossRef)
    }

    override suspend fun updateProductWithChips(product: Product, newChipIds: List<String>) {
        updateProduct(product)
        val existingChipIds = getChipIdsForProduct(product.id!!)
        val chipsToRemove = existingChipIds.minus(newChipIds)
        val chipsToAdd = newChipIds.minus(existingChipIds)
        chipsToRemove.forEach { chipId ->
            deleteProductChipCrossRef(
                ProductChipCrossRef(
                    productId = product.id!!,
                    chipId = chipId
                )
            )
        }
        chipsToAdd.forEach { chipId ->
            insertProductChipCrossRef(
                ProductChipCrossRef(
                    productId = product.id!!,
                    chipId = chipId
                )
            )
        }
    }

    override suspend fun getChipIdsForProduct(productId: String): List<String> {
        return withContext(Dispatchers.IO) {
        productDao.getChipIdsForProduct(productId)
        }
    }

    override suspend fun getRecords(): List<Record> {
        return withContext(Dispatchers.IO) {
            recordDao.getAllRecords()
        }
    }

    override suspend fun insertRecord(record: Record) {
        record.id = genId()
        recordDao.insertRecord(record)
    }

    override suspend fun updateRecord(record: Record) {
        recordDao.updateRecord(record)
    }

    override suspend fun deleteRecord(record: Record) {
        recordDao.deleteRecord(record)
    }

    override suspend fun getRecord(id: String): Record {
        return recordDao.getRecord(id)
    }

    override suspend fun getRecordListItems(): List<RecordListItem> {
        return withContext(Dispatchers.IO) {
            val baseList = recordDao.getRecordListItems()
            baseList.map { base ->
                val chipIds = recordDao.getChipIdsForProduct(base.record.productId)
                RecordListItem(
                    record = base.record,
                    name = base.name,
                    shortcut = base.shortcut,
                    typeId = base.typeId,
                    chipIds = chipIds,
                    iconRoute = base.iconRoute
                )
            }
        }
    }

    override suspend fun insertUnit(unit: WarehouseUnit) {
        unit.id = genId()
        unitDao.insertUnit(unit)
    }

    override suspend fun getUnits(): List<WarehouseUnit> {
        return withContext(Dispatchers.IO) {
            unitDao.getAllUnits()
        }
    }

    override suspend fun getUnitsSize(): Int {
        return unitDao.getUnitsSize()
    }

    override suspend fun getUnit(id: String): WarehouseUnit {
        return unitDao.getUnit(id)
    }

    override suspend fun updateUnit(unit: WarehouseUnit) {
        unitDao.updateUnit(unit)
    }

    override suspend fun deleteUnit(unit: WarehouseUnit) {
        unitDao.deleteUnit(unit)
    }

    override suspend fun getTypes(): List<Type> {
        return withContext(Dispatchers.IO) {
            typeDao.getAllTypes()
        }
    }

    override suspend fun insertType(type: Type) {
        type.id = genId()
        typeDao.insertType(type)
    }

    override suspend fun deleteType(type: Type) {
        typeDao.deleteType(type)
    }

    override suspend fun updateType(type: Type) {
        typeDao.updateType(type)
    }

    override suspend fun getType(id: String): Type {
        return typeDao.getType(id)
    }

    override suspend fun getChips(): List<Chip> {
        return withContext(Dispatchers.IO) {
            chipDao.getAllChips()
        }
    }

    override suspend fun insertChip(chip: Chip) : String {
        chip.id = genId()
        chipDao.insertChip(chip)
        return chip.id
    }

    override suspend fun updateChip(chip: Chip) {
        chipDao.updateChip(chip)
    }

    override suspend fun deleteChip(chip: Chip) {
        chipDao.deleteChip(chip)
    }

    override suspend fun getChip(id: String): Chip {
        return chipDao.getChip(id)
    }
    private fun genId() : String{
        return UUID.randomUUID().toString()
    }
}