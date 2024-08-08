package eu.glitch.mywarehouse.ui.screens.chip

sealed class AddEditChipUIState {
    object Loading : AddEditChipUIState()
    class ProductStateChanged(val data: AddEditChipData) : AddEditChipUIState()
    object Success : AddEditChipUIState()
}