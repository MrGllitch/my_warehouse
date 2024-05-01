package eu.glitch.mywarehouse.ui.screens.product

sealed class AddEditProductUIState {
    object Loading : AddEditProductUIState()
    class ProductStateChanges(val data: AddEditProductData) : AddEditProductUIState()
}