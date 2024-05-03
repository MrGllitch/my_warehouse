package eu.glitch.mywarehouse.ui.screens.product

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ILocalProductRepository
) : ViewModel(), ProductListActions {

    var data: ProductListData = ProductListData()

    private val _ProductListUIState: MutableStateFlow<ProductListUIState> = MutableStateFlow(value = ProductListUIState.Loading)
    val productListUIState = _ProductListUIState.asStateFlow()

}