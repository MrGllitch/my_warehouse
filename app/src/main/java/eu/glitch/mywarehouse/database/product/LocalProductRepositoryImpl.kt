package eu.glitch.mywarehouse.database.product

import eu.glitch.mywarehouse.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalProductRepositoryImpl @Inject constructor(private val productDao: ProductDao) :
    ILocalProductRepository {
    override fun getProducts(): Flow<Product> {
        return productDao.getAllProducts()
    }

    override suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product = product)
    }

}