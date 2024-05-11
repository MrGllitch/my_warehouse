package eu.glitch.mywarehouse.ui.screens.type

sealed class AddEditTypeUIState {
    object Loading : AddEditTypeUIState()
    class TypeStateChanged(val data: AddEditTypeData) : AddEditTypeUIState()
    object Success : AddEditTypeUIState()
}