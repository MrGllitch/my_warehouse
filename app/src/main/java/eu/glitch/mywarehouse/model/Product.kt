package eu.glitch.mywarehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Type::class,
            parentColumns = ["t_id"],
            childColumns = ["type_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = WarehouseUnit::class,
            parentColumns = ["u_id"],
            childColumns = ["unit_id"],
            onDelete = ForeignKey.SET_NULL
        ),
    ],
    indices = [Index(value = ["type_id", "unit_id"])]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "p_id")
    var id: Long? = null,
    @ColumnInfo(name = "unit_id")
    var unitId: Long,
    @ColumnInfo(name = "type_id")
    var typeId: Long,
    var name: String,
    var imageName: String?,
    var barcode: String?,

    )