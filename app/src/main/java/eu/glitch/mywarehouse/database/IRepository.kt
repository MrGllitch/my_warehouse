package eu.glitch.mywarehouse.database

import eu.glitch.mywarehouse.model.Chip
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.ProductChipCrossRef
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordListItem
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun isInitialized(): Boolean
    //products
    suspend fun getProducts(): List<Product>
    suspend fun insertProduct(product: Product) : String
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun getProduct(id: String): Product
    suspend fun insertProductChipCrossRef(crossRef: ProductChipCrossRef)
    suspend fun deleteProductChipCrossRef(crossRef: ProductChipCrossRef)
    suspend fun updateProductWithChips(product: Product, newChipIds: List<String>)
    suspend fun getChipIdsForProduct(productId: String): List<String>
    //records
    suspend fun getRecords(): List<Record>
    suspend fun insertRecord(record: Record)
    suspend fun updateRecord(record: Record)
    suspend fun deleteRecord(record: Record)
    suspend fun getRecord(id: String): Record
    suspend fun getRecordListItems(): List<RecordListItem>
    //units
    suspend fun insertUnit(unit: WarehouseUnit)
    suspend fun getUnits(): List<WarehouseUnit>
    suspend fun getUnitsSize(): Int
    suspend fun getUnit(id: String): WarehouseUnit
    suspend fun updateUnit(unit: WarehouseUnit)
    suspend fun deleteUnit(unit: WarehouseUnit)
    //types
    suspend fun getTypes(): List<Type>
    suspend fun insertType(type: Type)
    suspend fun deleteType(type: Type)
    suspend fun updateType(type: Type)
    suspend fun getType(id: String): Type
    //chips
    suspend fun getChips(): List<Chip>
    suspend fun insertChip(chip: Chip) : String
    suspend fun updateChip(chip: Chip)
    suspend fun deleteChip(chip: Chip)
    suspend fun getChip(id: String): Chip
}