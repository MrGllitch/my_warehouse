package eu.glitch.mywarehouse.ui.screens.unit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.unit.ILocalUnitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditUnitViewModel @Inject constructor(
    private val unitRepository: ILocalUnitRepository
) : ViewModel(), AddEditUnitActions {

    var data: AddEditUnitData = AddEditUnitData()

    private val _addEditProductUIState: MutableStateFlow<AddEditUnitUIState> =
        MutableStateFlow(value = AddEditUnitUIState.Loading)
    val addEditProductUIState = _addEditProductUIState.asStateFlow()
    override fun nameChanged(text: String) {
        data.unit.name = text
        _addEditProductUIState.update {
            AddEditUnitUIState.UnitStateChanged(data)
        }
    }

    override fun shortCutChanged(text: String) {
        data.unit.shortcut = text
        _addEditProductUIState.update {
            AddEditUnitUIState.UnitStateChanged(data)
        }
    }

    override fun addEditUnit() {
        if (data.unit.name.isEmpty()) {
            data.nameError = "Name cannot be empty"
            _addEditProductUIState.update {
                AddEditUnitUIState.UnitStateChanged(data)
            }
            return
        } else {
            data.nameError = null
        }
        if (data.unit.shortcut.isEmpty() || (data.unit.shortcut.length > 3 || data.unit.shortcut.length < 2)) {
            data.unitError = "Shortcut must have 2 or 3 letters"
            _addEditProductUIState.update {
                AddEditUnitUIState.UnitStateChanged(data)
            }
            return
        } else {
            data.unitError = null
        }
        if (data.unit.id == null) {
            viewModelScope.launch {
                unitRepository.insertUnit(data.unit)
            }
        } else {
            viewModelScope.launch {
                unitRepository.updateUnit(unit = data.unit)
            }
        }

        _addEditProductUIState.update {
            AddEditUnitUIState.Success
        }
    }

    override fun loadUnit(id: Long) {
        viewModelScope.launch {
            data.unit = unitRepository.getUnit(id)
        }
        _addEditProductUIState.update {
            AddEditUnitUIState.UnitStateChanged(data)
        }
    }

    override fun deleteUnit() {
        viewModelScope.launch {
            unitRepository.deleteUnit(data.unit)
        }
    }

}