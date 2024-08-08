package eu.glitch.mywarehouse.ui.screens.type

interface AddEditTypeActions {
    fun iconChanged(iconRoute: String)
    fun nameChanged(text: String)
    fun addEditType()
    fun deleteType()

}