package eu.glitch.mywarehouse.ui.screens.type

sealed class TypeListUIState {
    object Loading : TypeListUIState()
    class TypeListChanged(val data: TypeListData) : TypeListUIState()
}