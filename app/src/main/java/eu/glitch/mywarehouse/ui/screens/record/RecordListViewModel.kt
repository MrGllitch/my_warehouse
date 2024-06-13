package eu.glitch.mywarehouse.ui.screens.record

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.record.ILocalRecordRepository
import eu.glitch.mywarehouse.model.RecordDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val recordRepository: ILocalRecordRepository,

    ) : ViewModel(), RecordListActions {

    var data: RecordListData = RecordListData()


    private val _recordListUIState: MutableStateFlow<RecordListUIState> =
        MutableStateFlow(value = RecordListUIState.Loading)
    val recordListUIState = _recordListUIState.asStateFlow()


    override fun submitChanges() {
        data.editRecord!!.record.quantity = data.tempQuantity.toInt()
        Log.d("changing", "submitChanges: ${data.editRecord!!.record}")
        viewModelScope.launch {
            recordRepository.updateRecord(data.editRecord!!.record)
            loadRecordDetails()
        }
    }


    override fun quantityChanged(newQuantity: String) {
        data.tempQuantity = newQuantity
        _recordListUIState.update {
            RecordListUIState.RecordListChanged(data)
        }
    }

    override fun setEditedRecord(recordDetails: RecordDetails) {
        data.editRecord = recordDetails
        _recordListUIState.update {
            RecordListUIState.RecordListChanged(data)
        }
    }

    fun loadRecordDetails() {
        viewModelScope.launch {
            recordRepository.getRecordWithDetails().collect {
                data.records = it.toMutableList()

                data.records.forEach {
                    Log.d(
                        "loaded",
                        "record id: ${it.record.id} product id: ${it.product.id} unit id: ${it.unit.id}, type id: ${it.type.id}"
                    )
                }

                _recordListUIState.update {
                    RecordListUIState.RecordListChanged(data)
                }
                Log.d("loaded", "---------------")
            }
        }
    }
}