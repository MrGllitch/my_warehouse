package eu.glitch.mywarehouse.ui.screens.record

import eu.glitch.mywarehouse.model.RecordDetails

interface RecordListActions {
    //fun saveNameWLastName(lastName: String)
    fun quantityChanged(newQuantity: String)
    fun setEditedRecord(recordDetails: RecordDetails)
    fun submitChanges()
}