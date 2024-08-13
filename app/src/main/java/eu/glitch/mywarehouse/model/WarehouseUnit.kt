package eu.glitch.mywarehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "units")
data class WarehouseUnit(
    @PrimaryKey
    @ColumnInfo(name = "u_id")
    var id: String= "",

    var name: String ="",
    var shortcut: String="",
){
    constructor() : this("", "", "")
}

