package eu.glitch.mywarehouse.database.product

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.ProductChipCrossRef
import kotlinx.coroutines.flow.Flow


interface ILocalProductRepository {
    fun getProducts(): Flow<List<Product>>

    suspend fun insertProduct(product: Product): Long

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(product: Product)

    suspend fun getProduct(id: Long): Product

    suspend fun insertProductChipCrossRef(crossRef: ProductChipCrossRef)
    suspend fun deleteProductChipCrossRef(crossRef: ProductChipCrossRef)

    suspend fun updateProductWithChips(product: Product, newChipIds: List<Long>)
    suspend fun getChipIdsForProduct(productId: Long): List<Long>
}