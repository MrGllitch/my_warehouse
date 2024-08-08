package eu.glitch.mywarehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "product_chips", primaryKeys = ["p_id", "ch_id"])
data class ProductChipCrossRef(
    @ColumnInfo(name = "p_id")
    var productId: Long,

    @ColumnInfo(name = "ch_id")
    var chipId: Long,
)