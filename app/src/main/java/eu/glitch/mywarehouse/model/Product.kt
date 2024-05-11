package eu.glitch.mywarehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "products",
    foreignKeys = [ForeignKey(
        entity = Type::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("type_id"),
        onDelete = ForeignKey.SET_NULL
    )],
    indices = [Index(value = ["type_id"])]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String,
    @ColumnInfo(name = "type_id")
    var typeId: Long?,
    //todo photo
    //todo barcode
    var description: String? = null

)