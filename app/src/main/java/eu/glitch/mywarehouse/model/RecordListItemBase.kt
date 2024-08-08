package eu.glitch.mywarehouse.model

import androidx.room.Embedded


data class RecordListItemBase(
    @Embedded val record: Record,
    var name: String,
    var shortcut: String,
    var typeId: Long,
    var iconRoute: String,
)
