package eu.glitch.mywarehouse.ui.screens.record

sealed class AddEditRecordUIState {
    object Loading : AddEditRecordUIState()
    class RecordDataChanged(val data: AddEditRecordData) : AddEditRecordUIState()
    object Success : AddEditRecordUIState()
}