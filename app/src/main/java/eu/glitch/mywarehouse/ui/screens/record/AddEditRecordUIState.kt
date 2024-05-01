package eu.glitch.mywarehouse.ui.screens.record

sealed class AddEditRecordUIState {
    object Loading : AddEditRecordUIState()
    class ProductStateChanges(val data: AddEditRecordData) : AddEditRecordUIState()
}