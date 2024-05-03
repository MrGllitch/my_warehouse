package eu.glitch.mywarehouse.ui.screens.product

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import javax.inject.Inject

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    private val productRepository: ILocalProductRepository
) : ViewModel(), AddEditProductActions {

    var data: AddEditProductData = AddEditProductData()

    private val _addEditProductUIState: MutableStateFlow<AddEditProductUIState> = MutableStateFlow(value =  AddEditProductUIState.Loading)
    val addEditProductUIState = _addEditProductUIState.asStateFlow()

}