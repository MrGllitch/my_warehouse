package eu.glitch.mywarehouse.ui.screens.unit

interface AddEditUnitActions {
    //fun saveNameWLastName(lastName: String)
    fun nameChanged(text: String)
    fun shortCutChanged(text: String)
    fun addEditUnit()
    fun loadUnit(id: String)
    fun deleteUnit()
}