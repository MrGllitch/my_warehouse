package eu.glitch.mywarehouse.ui.screens.unit

sealed class UnitListUIState {
    object Loading : UnitListUIState()
    class UnitStateChangesList(val data: UnitListData) : UnitListUIState()
}