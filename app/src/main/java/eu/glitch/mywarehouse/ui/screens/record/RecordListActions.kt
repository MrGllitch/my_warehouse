package eu.glitch.mywarehouse.ui.screens.record

import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.RecordListItem

interface RecordListActions {
    //fun saveNameWLastName(lastName: String)
    fun quantityChanged(newQuantity: String)
    fun setEditedRecord(recordDetails: RecordListItem)
    fun editFilter(name: String, id: String, add: Boolean)
    fun submitChanges()
    fun removeOne(record: Record)
}
