package eu.glitch.mywarehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "records",
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = ["id"],
        //probably shoud be "productId" if so rework this
        childColumns = ["product_id"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = WarehouseUnit::class,
            parentColumns = ["id"],
            childColumns = ["unit_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["product_id", "unit_id"])]
)
data class Record(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "product_id")
    var productId: Long,
    @ColumnInfo(name = "unit_id")
    var unitId: Long? = 0,

    var quantity: Int = 0,

    //Millisecond from 1.1.1970
    @ColumnInfo(name = "date_from")
    var dateFrom: Long,
    @ColumnInfo(name = "date_to")
    var dateTo: Long
) {}