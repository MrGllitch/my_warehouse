package eu.glitch.mywarehouse.model

import androidx.room.Embedded


data class RecordListItem(
    @Embedded val record: Record,
    var name: String,
    var shortcut: String,
    var typeId: Long,
    var chipIds: List<Long>,
    var iconRoute: String,
)
