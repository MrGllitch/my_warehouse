package eu.glitch.mywarehouse.ui.screens.record

import eu.glitch.mywarehouse.model.RecordListItem

interface RecordListActions {
    //fun saveNameWLastName(lastName: String)
    fun quantityChanged(newQuantity: String)
    fun setEditedRecord(recordDetails: RecordListItem)
    fun editFilter(name: String, id: Long, add: Boolean)
    fun submitChanges()
}
