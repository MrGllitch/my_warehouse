package eu.glitch.mywarehouse.ui.screens.record

import eu.glitch.mywarehouse.model.Record

interface RecordListActions {
    //fun saveNameWLastName(lastName: String)
    fun quantityChanged(newQuantity: String)
    fun setEditedRecord(record: Record)
    fun submitChanges()
}