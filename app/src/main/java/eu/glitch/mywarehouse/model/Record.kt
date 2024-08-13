package eu.glitch.mywarehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "records",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["p_id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index(value = ["product_id"])]
)
data class Record(
    @PrimaryKey
    @ColumnInfo(name = "r_id")
    var id: String = "",
    @ColumnInfo(name = "product_id")
    var productId: String = "",

    var startQuantity: Int = 0,
    var quantity: Int = 0,

    //Millisecond from 1.1.1970
    @ColumnInfo(name = "date_from")
    var dateFrom: Long,
    @ColumnInfo(name = "date_to")
    var dateTo: Long,
){
    constructor(): this("","",0,0,0,0)
}