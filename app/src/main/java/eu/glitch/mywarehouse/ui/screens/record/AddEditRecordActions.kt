package eu.glitch.mywarehouse.ui.screens.record

interface AddEditRecordActions {
    fun productChanged(id: String)
    fun quantityChanged(quantity: String)
    fun dateToChanged(dateTo: String)

    fun addEditRecord()
}