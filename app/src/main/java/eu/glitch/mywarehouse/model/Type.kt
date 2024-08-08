package eu.glitch.mywarehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.glitch.mywarehouse.ui.elements.FilterItem

@Entity(tableName = "types")
data class Type(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "t_id")
    override var id: Long? = null,

    override var name: String,
    var iconRoute: String,
) : FilterItem