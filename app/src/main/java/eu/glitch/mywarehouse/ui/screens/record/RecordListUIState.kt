package eu.glitch.mywarehouse.ui.screens.record

sealed class RecordListUIState {
    object Loading : RecordListUIState()
    class RecordListChanged(val data: RecordListData) : RecordListUIState()

    data class Error(val message: String) : RecordListUIState()
}