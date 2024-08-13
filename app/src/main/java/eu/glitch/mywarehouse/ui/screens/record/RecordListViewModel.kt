package eu.glitch.mywarehouse.ui.screens.record

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.database.impl.RepositoryImpl

import eu.glitch.mywarehouse.model.RecordListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.full.memberProperties

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel(), RecordListActions {
    var data: RecordListData = RecordListData()


    private val _recordListUIState: MutableStateFlow<RecordListUIState> =
        MutableStateFlow(value = RecordListUIState.Loading)
    val recordListUIState = _recordListUIState.asStateFlow()


    override fun submitChanges() {
        try {
            data.editRecord!!.quantity += data.tempQuantityChange.toInt()
        } catch (e: Exception) {
            Log.e("invalid number", "cannot convert ${data.tempQuantityChange} to int")
        }
        viewModelScope.launch {
            repository.updateRecord(data.editRecord!!)
            loadRecords()
        }
    }


    override fun quantityChanged(newQuantity: String) {
        data.tempQuantityChange = newQuantity
        _recordListUIState.update {
            RecordListUIState.RecordListChanged(data)
        }
    }

    override fun setEditedRecord(item: RecordListItem) {
        data.editRecord = item.record
        data.editRecordName = item.name
        data.editRecordShortcut = item.shortcut
        _recordListUIState.update {
            RecordListUIState.RecordListChanged(data)
        }
    }

    override fun editFilter(name: String, id: String, add: Boolean) {

        if (data.filters.containsKey(key = name)) {
            //if there is already key with name
            var nameFilters = data.filters.get(name)
            if (add) {
                nameFilters!!.add(id)
                data.filters[name] = nameFilters
            } else {
                nameFilters!!.remove(id)
                if (nameFilters.isEmpty()) {
                    data.filters.remove(name)
                } else {
                    data.filters[name] = nameFilters
                }
            }

        } else {
            //if not
            data.filters.put(name, mutableListOf(id))
        }
        Log.d("filter", "filters: ${data.filters}")
        filterRecords()
        _recordListUIState.update {
            RecordListUIState.RecordListChanged(data)
        }

    }

    private fun filterRecords() {
        val filteredSet = mutableSetOf<RecordListItem>()
        data.filteredRecords.clear()
        
        if (data.filters.isEmpty()) {
            data.filteredRecords.addAll(data.records)
            return
        }

        data.records.forEach { record ->
            for (filter in data.filters) {
                val property = RecordListItem::class.memberProperties.find { it.name == filter.key }
                Log.d("filter", "property  ${property.toString()}")
                if (property != null) {
                    val attributeValue = property.get(record)
                    Log.d("filter", "attributeValue  $attributeValue")
                    if (attributeValue != null) {
                        when (attributeValue) {
                            is List<*> -> {
                                if (attributeValue.any { it in filter.value }) {
                                    filteredSet.add(record)
                                }
                            }

                            else -> {
                                if (filter.value.contains(attributeValue)) {
                                    filteredSet.add(record)
                                }
                            }
                        }
                    }
                }
            }
        }
        data.filteredRecords.addAll(filteredSet)
    }

    fun loadRecords() {
        viewModelScope.launch {
            data.records = repository.getRecordListItems().toMutableList()
            val filteredList = data.records.filter { it.record.quantity > 0 }
            data.records = filteredList.toMutableList()

            filterRecords()

            _recordListUIState.update {
                RecordListUIState.RecordListChanged(data)
            }
        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            data.types = repository.getTypes().toMutableList()


                _recordListUIState.update {
                    RecordListUIState.RecordListChanged(data)
                }

        }
    }

    fun loadChips() {
        viewModelScope.launch {
            data.chips = repository.getChips().toMutableList()


                _recordListUIState.update {
                    RecordListUIState.RecordListChanged(data)
                }
            }

    }
}