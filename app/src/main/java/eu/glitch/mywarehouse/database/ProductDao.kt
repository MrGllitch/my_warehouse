package eu.glitch.mywarehouse.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.glitch.mywarehouse.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): Flow<Product>

    @Insert
    suspend fun insert(product: Product) : Long

    @Delete
    suspend fun delete(product: Product)

    @Update
    suspend fun update(product: Product) : Int
}