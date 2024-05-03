package eu.glitch.mywarehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "records",
    foreignKeys =[ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("id"),
        //probably shoud be "productId" if so rework this
        childColumns = arrayOf("product_id"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = WarehouseUnit::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("unit_id"),
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
){}