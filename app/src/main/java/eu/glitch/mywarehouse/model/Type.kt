package eu.glitch.mywarehouse.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "types")
data class Type(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var name: String,
    var iconRoute: String,
    var consumable: Boolean = false
)