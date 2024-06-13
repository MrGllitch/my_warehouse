package eu.glitch.mywarehouse.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import eu.glitch.mywarehouse.database.type.ILocalTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ILocalProductRepository,
    private val typeRepository: ILocalTypeRepository
) : ViewModel(), ProductListActions {

    var data: ProductListData = ProductListData()

    private val _productListUIState: MutableStateFlow<ProductListUIState> =
        MutableStateFlow(value = ProductListUIState.Loading)
    val productListUIState = _productListUIState.asStateFlow()


    fun loadProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect {
                data.products = it.toMutableList()
                _productListUIState.update {
                    ProductListUIState.ProductListChanged(data)
                }
            }

        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            typeRepository.getTypes().collect {
                data.types = it.toMutableList()
                _productListUIState.update {
                    ProductListUIState.ProductListChanged(data)
                }
            }

        }
    }
}