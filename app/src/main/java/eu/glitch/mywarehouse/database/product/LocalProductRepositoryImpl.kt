package eu.glitch.mywarehouse.database.product

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.ProductChipCrossRef
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalProductRepositoryImpl @Inject constructor(private val productDao: ProductDao) :
    ILocalProductRepository {
    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }

    override suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product = product)
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product = product)
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    override suspend fun getProduct(id: Long): Product {
        return productDao.getProduct(id)
    }

    override suspend fun insertProductChipCrossRef(crossRef: ProductChipCrossRef) {
        productDao.insertProductChipCrossRef(crossRef = crossRef)
    }

    override suspend fun deleteProductChipCrossRef(crossRef: ProductChipCrossRef) {
        productDao.deleteProductChipCrossRef(crossRef = crossRef)
    }

    override suspend fun getChipIdsForProduct(productId: Long): List<Long> {
        return productDao.getChipIdsForProduct(productId = productId)
    }

    override suspend fun updateProductWithChips(product: Product, newChipIds: List<Long>) {
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

}