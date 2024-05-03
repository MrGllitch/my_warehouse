package eu.glitch.mywarehouse.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "types")
data class Type(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var name: String,
    //todo icon
    var consumable: Boolean = false
)