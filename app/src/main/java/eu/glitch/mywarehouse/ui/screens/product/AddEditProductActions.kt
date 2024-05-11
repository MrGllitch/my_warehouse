package eu.glitch.mywarehouse.ui.screens.product

interface AddEditProductActions {
    fun nameChanged(text: String)
    fun descriptionChanged(text: String)
    fun typeChanged(id: Long)

    fun addEditProduct()
    fun deleteProduct()
}