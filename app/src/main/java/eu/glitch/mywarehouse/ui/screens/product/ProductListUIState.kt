package eu.glitch.mywarehouse.ui.screens.product

sealed class ProductListUIState {
    object Loading : ProductListUIState()
    class ProductListChanged(val data: ProductListData) : ProductListUIState()
}