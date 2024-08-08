package eu.glitch.mywarehouse.ui.screens.unit

sealed class AddEditUnitUIState {
    object Loading : AddEditUnitUIState()
    class UnitStateChanged(val data: AddEditUnitData) : AddEditUnitUIState()
    object Success : AddEditUnitUIState()
}