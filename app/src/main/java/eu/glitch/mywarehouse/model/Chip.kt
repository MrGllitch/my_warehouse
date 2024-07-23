package eu.glitch.mywarehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.glitch.mywarehouse.ui.elements.FilterItem

@Entity(tableName = "chips")
data class Chip(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ch_id")
    override var id: Long? = null,

    override var name: String,
) : FilterItem