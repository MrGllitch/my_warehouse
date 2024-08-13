package eu.glitch.mywarehouse.ui.screens.record

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.glitch.mywarehouse.database.impl.RepositoryImpl
import eu.glitch.mywarehouse.model.Type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRecordViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    @ApplicationContext private val context: Context,
) : ViewModel(), AddEditRecordActions {

    var data: AddEditRecordData = AddEditRecordData()
    var types: List<Type> = listOf()

    private val _addEditRecordUIState: MutableStateFlow<AddEditRecordUIState> =
        MutableStateFlow(value = AddEditRecordUIState.Loading)
    val addEditProductUIState = _addEditRecordUIState.asStateFlow()


    override fun productChanged(id: String) {
        data.record.productId = id
        _addEditRecordUIState.update {
            AddEditRecordUIState.RecordDataChanged(data)
        }
    }


    override fun quantityChanged(quantity: String) {
        val newQuantity = quantity.toIntOrNull() ?: 0
        data.record.quantity = newQuantity.coerceAtLeast(0)
        _addEditRecordUIState.update {
            AddEditRecordUIState.RecordDataChanged(data)
        }
    }

    override fun dateToChanged(dateTo: String) {
        data.record.dateTo = dateTo.toLong()
        _addEditRecordUIState.update {
            AddEditRecordUIState.RecordDataChanged(data)
        }
    }

    fun barcodePrepopulate(code: String) {
        Log.d("barcode", "trying to find product: ${data.products} ")
        val product = data.products.find { it.barcode == code }
        if (product != null) {
            productChanged(product!!.id.toString())
        }
        _addEditRecordUIState.update {
            AddEditRecordUIState.RecordDataChanged(data)
        }
    }

    override fun addEditRecord() {
        if (data.record.productId == "") {
            data.productError = "You have to select product"
            _addEditRecordUIState.update {
                AddEditRecordUIState.RecordDataChanged(data)
            }
            return
        } else {
            data.productError = null
        }
        if (data.record.quantity == 0) {
            data.quantityError = "Number must be greater than 0"
            _addEditRecordUIState.update {
                AddEditRecordUIState.RecordDataChanged(data)
            }
            return
        } else {
            data.quantityError = null
        }

        if (data.record.id == "") {
            data.record.dateFrom = System.currentTimeMillis()
        }

        data.record.startQuantity = data.record.quantity

        viewModelScope.launch {
            if (data.record.id == "") {
                repository.insertRecord(data.record)
            } else {

                repository.updateRecord(data.record)
            }
            _addEditRecordUIState.update {
                AddEditRecordUIState.Success
            }
        }
    }


    fun loadProducts() {
        viewModelScope.launch {
            data.products = repository.getProducts().toMutableList()
            _addEditRecordUIState.update {
                AddEditRecordUIState.RecordDataChanged(data)
            }
        }
    }


    fun loadUnits() {
        viewModelScope.launch {
            data.units = repository.getUnits().toMutableList()
            _addEditRecordUIState.update {
                AddEditRecordUIState.RecordDataChanged(data)
            }

        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            types = repository.getTypes().toMutableList()
            _addEditRecordUIState.update {
                AddEditRecordUIState.RecordDataChanged(data)
            }
        }

    }

    fun loadRecord(id: String) {
        //todo
    }

}