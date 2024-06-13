package eu.glitch.mywarehouse.model

import androidx.room.Embedded
import androidx.room.Relation


data class RecordDetails(
    @Embedded val record: Record,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "p_id",
        entity = Product::class
    )
    val product: Product,
    @Relation(
        parentColumn = "unit_id",
        entityColumn = "u_id",
        entity = WarehouseUnit::class
    )
    val unit: WarehouseUnit,
    @Relation(
        parentColumn = "type_id",
        entityColumn = "t_id",
        entity = Type::class
    )
    val type: Type

)
