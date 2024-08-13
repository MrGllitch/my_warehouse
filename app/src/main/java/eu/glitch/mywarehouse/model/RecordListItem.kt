package eu.glitch.mywarehouse.model

import androidx.room.Embedded


data class RecordListItem(
    @Embedded val record: Record,
    var name: String,
    var shortcut: String,
    var typeId: String,
    var chipIds: List<String>,
    var iconRoute: String,
)
