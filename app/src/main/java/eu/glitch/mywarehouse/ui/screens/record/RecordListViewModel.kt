package eu.glitch.mywarehouse.ui.screens.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.record.ILocalRecordRepository
import eu.glitch.mywarehouse.model.Record
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val recordRepository: ILocalRecordRepository
) : ViewModel(), RecordListActions {

    var data: RecordListData = RecordListData()

    private val _recordListUIState: MutableStateFlow<RecordListUIState> =
        MutableStateFlow(value = RecordListUIState.Loading)
    val recordListUIState = _recordListUIState.asStateFlow()

    fun loadRecords() {
        viewModelScope.launch {
            recordRepository.getRecords().collect { records ->
                val sortedRecords = records.sortedBy { it.dateTo } // Sort by dateTo
                data.records = sortedRecords.toMutableList()
                _recordListUIState.update {
                    RecordListUIState.RecordListChanged(data)
                }
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            recordRepository.getProducts().collect {
                data.products = it.toMutableList()
                _recordListUIState.update {
                    RecordListUIState.RecordListChanged(data)
                }
            }

        }
    }

    fun loadUnits() {
        viewModelScope.launch {
            recordRepository.getUnits().collect {
                data.units = it.toMutableList()
                _recordListUIState.update {
                    RecordListUIState.RecordListChanged(data)
                }
            }

        }
    }

    override fun quantityChanged(newQuantity: String) {
        data.tempQuantity = newQuantity
        _recordListUIState.update {
            RecordListUIState.RecordListChanged(data)
        }
    }

    override fun setEditedRecord(record: Record) {
        data.editRecord = record
        _recordListUIState.update {
            RecordListUIState.RecordListChanged(data)
        }
    }

    override fun submitChanges() {
        data.editRecord!!.quantity = data.tempQuantity.toInt()
        viewModelScope.launch {
            recordRepository.updateRecord(data.editRecord!!)
        }
        loadRecords()
    }
}