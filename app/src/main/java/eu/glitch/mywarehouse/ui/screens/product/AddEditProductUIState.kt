package eu.glitch.mywarehouse.ui.screens.product

sealed class AddEditProductUIState {
    object Loading : AddEditProductUIState()
    class ProductStateChanged(val data: AddEditProductData) : AddEditProductUIState()
    object Success : AddEditProductUIState()
}