package eu.glitch.mywarehouse.database.product

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Type
import kotlinx.coroutines.flow.Flow


interface ILocalProductRepository {
    fun getProducts(): Flow<List<Product>>

    suspend fun insertProduct(product: Product): Long

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(product: Product)

    suspend fun getProduct(id: Long): Product

    fun getTypes(): Flow<List<Type>>
}