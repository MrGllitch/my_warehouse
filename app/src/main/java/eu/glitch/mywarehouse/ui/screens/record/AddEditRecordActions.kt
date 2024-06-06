package eu.glitch.mywarehouse.ui.screens.record

interface AddEditRecordActions {
    fun productChanged(id: String)
    fun unitChanged(id: String)
    fun quantityChanged(quantity: String)
    fun dateFromChanged(dateFrom: String)
    fun dateToChanged(dateTo: String)

    fun addEditProduct()
    fun deleteProduct()

}