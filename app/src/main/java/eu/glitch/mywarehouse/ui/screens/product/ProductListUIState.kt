package eu.glitch.mywarehouse.ui.screens.product

sealed class ProductListUIState {
    object Loading : ProductListUIState()
    class ProductStateChangesList(val data: ProductListData) : ProductListUIState()
}