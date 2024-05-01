package eu.glitch.mywarehouse.ui.screens.record

sealed class RecordListUIState {
    object Loading : RecordListUIState()
    class RecordStateChangesList(val data: RecordListData) : RecordListUIState()
}