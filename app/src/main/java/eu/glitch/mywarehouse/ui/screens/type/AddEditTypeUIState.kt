package eu.glitch.mywarehouse.ui.screens.type

sealed class AddEditTypeUIState {
    object Loading : AddEditTypeUIState()
    class TypeStateChanges(val data: AddEditTypeData) : AddEditTypeUIState()
}