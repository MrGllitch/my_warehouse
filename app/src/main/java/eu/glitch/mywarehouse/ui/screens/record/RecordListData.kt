package eu.glitch.mywarehouse.ui.screens.record

import eu.glitch.mywarehouse.model.RecordDetails

class RecordListData {
    var records: MutableList<RecordDetails> = mutableListOf()


    var editRecord: RecordDetails? = null
    var tempQuantity: String = ""
    var editRecordQuantityError: String? = null
}