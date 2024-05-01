package eu.glitch.mywarehouse.ui.screens.unit

sealed class AddEditUnitUIState {
    object Loading : AddEditUnitUIState()
    class UnitStateChanges(val data: AddEditUnitData) : AddEditUnitUIState()
}