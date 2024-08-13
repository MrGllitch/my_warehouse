package eu.glitch.mywarehouse.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.ProductChipCrossRef

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<Product>

    @Insert
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM products WHERE p_id = :id")
    suspend fun getProduct(id: String): Product

    @Insert
    suspend fun insertProductChipCrossRef(crossRef: ProductChipCrossRef)

    @Delete
    suspend fun deleteProductChipCrossRef(crossRef: ProductChipCrossRef)

    @Query("SELECT ch_id FROM product_chips WHERE p_id = :productId")
    suspend fun getChipIdsForProduct(productId: String): List<String>
}