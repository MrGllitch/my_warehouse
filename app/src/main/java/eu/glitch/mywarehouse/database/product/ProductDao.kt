package eu.glitch.mywarehouse.database.product

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.ProductChipCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Insert
    suspend fun insertProduct(product: Product): Long

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product): Int

    @Query("SELECT * FROM products WHERE p_id = :id")
    suspend fun getProduct(id: Long): Product

    @Insert
    suspend fun insertProductChipCrossRef(crossRef: ProductChipCrossRef)

    @Delete
    suspend fun deleteProductChipCrossRef(crossRef: ProductChipCrossRef)

    @Query("SELECT ch_id FROM product_chips WHERE p_id = :productId")
    suspend fun getChipIdsForProduct(productId: Long): List<Long>
}