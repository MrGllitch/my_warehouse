package eu.glitch.mywarehouse.database.product

import eu.glitch.mywarehouse.model.Product
import kotlinx.coroutines.flow.Flow


interface ILocalProductRepository {
    fun getProducts(): Flow<Product>

    suspend fun insertProduct(product: Product) : Long

}