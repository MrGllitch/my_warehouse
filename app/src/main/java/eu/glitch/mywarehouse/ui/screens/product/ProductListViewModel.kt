package eu.glitch.mywarehouse.ui.screens.product

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import eu.glitch.mywarehouse.database.IRepository
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel(), ProductListActions {

    var data: ProductListData = ProductListData()

    private val _ProductListUIState: MutableStateFlow<ProductListUIState> = MutableStateFlow(value = ProductListUIState.Loading)
    val productListUIState = _ProductListUIState.asStateFlow()

}