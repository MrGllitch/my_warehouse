package eu.glitch.mywarehouse.ui.screens.chip

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.glitch.mywarehouse.database.chip.ILocalChipRepository
import eu.glitch.mywarehouse.model.Chip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditChipViewModel @Inject constructor(
    private val chipRepository: ILocalChipRepository,
    @ApplicationContext val context: Context
) : ViewModel(), AddEditChipActions {


    var data: AddEditChipData = AddEditChipData()

    private val _addEditChipUIState: MutableStateFlow<AddEditChipUIState> =
        MutableStateFlow(value = AddEditChipUIState.Loading)
    val addEditChipUIState = _addEditChipUIState.asStateFlow()


    fun loadChips() {
        viewModelScope.launch {
            chipRepository.getChips().collect() {
                data.chips = it.toMutableList()
                _addEditChipUIState.update {
                    AddEditChipUIState.ProductStateChanged(data)
                }
            }
        }
    }

    override fun nameChanged(text: String) {
        data.chip.name = text
        _addEditChipUIState.update {
            AddEditChipUIState.ProductStateChanged(data)
        }
    }

    override fun addEditChip(): Boolean {
        if (data.chip.name.length < 3) {
            data.nameError = "name has to be at least 3 long"
            _addEditChipUIState.update {
                AddEditChipUIState.ProductStateChanged(data)
            }
            return false
        } else {
            data.nameError = null
        }
        viewModelScope.launch {
            if (data.chip.id == null) {
                chipRepository.insertChip(data.chip)
            } else {
                chipRepository.updateChip(data.chip)
            }
            loadChips()
        }
        return true
    }

    fun newChipCreation() {
        data.chip = Chip(null, "")
        _addEditChipUIState.update {
            AddEditChipUIState.ProductStateChanged(data)
        }
    }

    override fun deleteChip() {
        viewModelScope.launch {
            chipRepository.deleteChip(data.chip)
            loadChips()
        }
        _addEditChipUIState.update {
            AddEditChipUIState.ProductStateChanged(data)
        }
    }

    override fun isAddEditOpen(state: Boolean) {
        data.isAddEditOpen = state
        _addEditChipUIState.update {
            AddEditChipUIState.ProductStateChanged(data)
        }
    }

    override fun editChipChanged(chip: Chip) {
        data.chip = chip
        _addEditChipUIState.update {
            AddEditChipUIState.ProductStateChanged(data)
        }
    }
}