package eu.glitch.mywarehouse.ui.screens.type

sealed class TypeListUIState {
    object Loading : TypeListUIState()
    class ProductStateChangesList(val data: TypeListData) : TypeListUIState()
}