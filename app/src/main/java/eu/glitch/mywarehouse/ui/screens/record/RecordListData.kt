package eu.glitch.mywarehouse.ui.screens.record

import eu.glitch.mywarehouse.model.Chip
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordListItem
import eu.glitch.mywarehouse.model.Type

class RecordListData {
    var records: MutableList<RecordListItem> = mutableListOf()
    var filteredRecords: MutableList<RecordListItem> = mutableListOf()
    var types: MutableList<Type> = mutableListOf()
    var chips: MutableList<Chip> = mutableListOf()

    var filters: MutableMap<String, MutableList<Long>> = mutableMapOf()


    var editRecordName: String = ""
    var editRecordShortcut: String = ""
    var editRecord: Record? = null
    var tempQuantityChange: String = ""
    var editRecordQuantityError: String? = null
}