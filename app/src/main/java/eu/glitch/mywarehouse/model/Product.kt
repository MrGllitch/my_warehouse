package eu.glitch.mywarehouse.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(var name: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    var description: String? = null

}