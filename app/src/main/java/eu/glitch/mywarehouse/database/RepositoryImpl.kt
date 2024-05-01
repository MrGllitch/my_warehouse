package eu.glitch.mywarehouse.database

import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.database.ProductDao
import eu.glitch.mywarehouse.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val productDao: ProductDao) : IRepository {
    override fun getProducts(): Flow<Product> {
        return productDao.getAll()
    }

    override suspend fun insertProduct(product: Product): Long {
        return productDao.insert(product = product)
    }

}