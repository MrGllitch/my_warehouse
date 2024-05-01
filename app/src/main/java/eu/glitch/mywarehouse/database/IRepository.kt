package eu.glitch.mywarehouse.database

import eu.glitch.mywarehouse.model.Product
import kotlinx.coroutines.flow.Flow


interface IRepository {
    fun getProducts(): Flow<Product>

    suspend fun insertProduct(product: Product) : Long

}