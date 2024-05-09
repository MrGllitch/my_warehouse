package eu.glitch.mywarehouse.ui.screens.unit

sealed class UnitListUIState {
    object Loading : UnitListUIState()
    class UnitStateChanged(val data: UnitListData) : UnitListUIState()
}