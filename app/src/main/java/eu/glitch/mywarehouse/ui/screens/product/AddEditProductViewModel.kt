package eu.glitch.mywarehouse.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    private val productRepository: ILocalProductRepository
) : ViewModel(), AddEditProductActions {

    var data: AddEditProductData = AddEditProductData()

    private val _addEditProductUIState: MutableStateFlow<AddEditProductUIState> =
        MutableStateFlow(value = AddEditProductUIState.Loading)
    val addEditProductUIState = _addEditProductUIState.asStateFlow()
    override fun nameChanged(text: String) {
        data.product.name = text
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    override fun descriptionChanged(text: String) {
        data.product.description = text
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    override fun typeChanged(id: Long) {
        data.product.typeId = id
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    override fun addEditProduct() {
        if (data.product.name.isEmpty()) {
            data.nameError = "Name cannot be empty"
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
            return
        } else {
            data.nameError = null
        }
        if (data.product.typeId == null) {
            data.typeError = "You have to select type"
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
            return
        } else {
            data.typeError = null
        }
        if (data.product.id == null) {
            viewModelScope.launch {
                productRepository.insertProduct(data.product)
            }
        } else {
            viewModelScope.launch {
                productRepository.updateProduct(data.product)
            }
        }

        _addEditProductUIState.update {
            AddEditProductUIState.Success
        }
    }

    override fun deleteProduct() {
        viewModelScope.launch {
            productRepository.deleteProduct(data.product)
        }
        _addEditProductUIState.update {
            AddEditProductUIState.Success
        }
    }

    fun loadProduct(id: Long) {

        viewModelScope.launch {
            data.product = productRepository.getProduct(id)
        }
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            productRepository.getTypes().collect() {
                data.types = it.toMutableList()
                _addEditProductUIState.update {
                    AddEditProductUIState.ProductStateChanged(data)
                }
            }
        }
    }

}