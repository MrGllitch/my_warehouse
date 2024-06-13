package eu.glitch.mywarehouse.ui.screens.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import eu.glitch.mywarehouse.database.record.ILocalRecordRepository
import eu.glitch.mywarehouse.database.type.ILocalTypeRepository
import eu.glitch.mywarehouse.database.unit.ILocalUnitRepository
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.Type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRecordViewModel @Inject constructor(
    private val recordRepository: ILocalRecordRepository,
    private val productRepository: ILocalProductRepository,
    private val unitRepository: ILocalUnitRepository,
    private val typeRepository: ILocalTypeRepository
) : ViewModel(), AddEditRecordActions {

    var data: AddEditRecordData = AddEditRecordData()
    var types: List<Type> = listOf()

    private val _addEditRecordUIState: MutableStateFlow<AddEditRecordUIState> =
        MutableStateFlow(value = AddEditRecordUIState.Loading)
    val addEditProductUIState = _addEditRecordUIState.asStateFlow()


    override fun productChanged(id: String) {
        data.record.productId = id
        val type = types.find { it.id == id.toLong() }
        data.record.typeId = type?.id.toString()
        _addEditRecordUIState.update {
            AddEditRecordUIState.RecordDataChanged(data)
        }
    }

    override fun unitChanged(id: String) {
        data.record.unitId = id
        _addEditRecordUIState.update {
            AddEditRecordUIState.RecordDataChanged(data)
        }
    }

    override fun quantityChanged(quantity: String) {
        val newQuantity = quantity.toIntOrNull() ?: 0
        data.record.quantity = newQuantity.coerceAtLeast(0).toString()
        _addEditRecordUIState.update {
            AddEditRecordUIState.RecordDataChanged(data)
        }
    }

    override fun dateFromChanged(dateFrom: String) {
        data.record.dateFrom = dateFrom
        _addEditRecordUIState.update {
            AddEditRecordUIState.RecordDataChanged(data)
        }
    }

    override fun dateToChanged(dateTo: String) {
        data.record.dateTo = dateTo
        _addEditRecordUIState.update {
            AddEditRecordUIState.RecordDataChanged(data)
        }
    }

    private fun isPositiveLong(str: String): Boolean {
        return try {
            val longValue = str.toLong()
            longValue > 0
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    override fun addEditRecord() {
        if (!isPositiveLong(data.record.productId)) {
            data.productError = "You have to select product"
            _addEditRecordUIState.update {
                AddEditRecordUIState.RecordDataChanged(data)
            }
            return
        } else {
            data.productError = null
        }
        if (!isPositiveLong(data.record.unitId)) {
            data.unitError = "You have to select unit"
            _addEditRecordUIState.update {
                AddEditRecordUIState.RecordDataChanged(data)
            }
            return
        } else {
            data.unitError = null
        }
        if (data.record.quantity == "0") {
            data.quantityError = "Number must be greater than 0"
            _addEditRecordUIState.update {
                AddEditRecordUIState.RecordDataChanged(data)
            }
            return
        } else {
            data.quantityError = null
        }

        //if creating new item set current time if edditing use that last
        val currentMillis = if (data.record.dateFrom != null) {
            data.record.dateFrom!!.toLong()
        } else {
            System.currentTimeMillis()
        }


        var newRecord = Record(
            id = null,
            productId = data.record.productId.toLong(),
            unitId = data.record.unitId.toLong(),
            typeId = data.record.typeId.toLong(),
            quantity = data.record.quantity.toInt(),
            startQuantity = data.record.quantity.toInt(),
            dateFrom = currentMillis,
            dateTo = data.record.dateTo.toLong()
        )
        if (data.record.id == null) {
            viewModelScope.launch {
                recordRepository.insertRecord(newRecord)
            }
        } else {
            viewModelScope.launch {
                recordRepository.updateRecord(newRecord)
            }
        }
        _addEditRecordUIState.update {
            AddEditRecordUIState.Success
        }
    }


    override fun deleteRecord() {
        //TODO("Not yet implemented")
    }

    fun loadProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect() {
                data.products = it.toMutableList()
                _addEditRecordUIState.update {
                    AddEditRecordUIState.RecordDataChanged(data)
                }
            }
        }
    }


    fun loadUnits() {
        viewModelScope.launch {
            unitRepository.getUnits().collect() {
                data.units = it.toMutableList()
                _addEditRecordUIState.update {
                    AddEditRecordUIState.RecordDataChanged(data)
                }
            }
        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            typeRepository.getTypes().collect() {
                types = it.toMutableList()
            }
        }
    }

    fun loadRecord(id: Long) {
        //todo this shit
    }

}