package eu.glitch.mywarehouse.ui.screens.type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.type.ILocalTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTypeViewModel @Inject constructor(
    private val typeRepository: ILocalTypeRepository
) : ViewModel(), AddEditTypeActions {

    var data: AddEditTypeData = AddEditTypeData()

    private val _addEditTypeUIState: MutableStateFlow<AddEditTypeUIState> =
        MutableStateFlow(value = AddEditTypeUIState.Loading)
    val addEditProductUIState = _addEditTypeUIState.asStateFlow()
    override fun iconChanged(iconRoute: String) {
        data.type.iconRoute = iconRoute
        _addEditTypeUIState.update {
            AddEditTypeUIState.TypeStateChanged(data)
        }
    }

    override fun nameChanged(text: String) {
        data.type.name = text
        _addEditTypeUIState.update {
            AddEditTypeUIState.TypeStateChanged(data)
        }
    }

    override fun addEditType() {
        if (data.type.name.isEmpty()) {
            data.nameError = "Name cannot be empty"
            _addEditTypeUIState.update {
                AddEditTypeUIState.TypeStateChanged(data)
            }
            return
        } else {
            data.nameError = null
        }
        if (data.type.iconRoute.isEmpty()) {
            data.iconError = "You have to select icon"
            _addEditTypeUIState.update {
                AddEditTypeUIState.TypeStateChanged(data)
            }
            return
        } else {
            data.iconError = null
        }
        if (data.type.id == null) {
            viewModelScope.launch {
                typeRepository.insertType(data.type)
            }
        } else {
            viewModelScope.launch {
                typeRepository.updateType(type = data.type)
            }
        }

        _addEditTypeUIState.update {
            AddEditTypeUIState.Success
        }
    }

    override fun deleteType() {
        viewModelScope.launch {
            typeRepository.deleteType(data.type)
        }
        _addEditTypeUIState.update {
            AddEditTypeUIState.Success
        }
    }


    fun loadType(id: Long) {
        viewModelScope.launch {
            data.type = typeRepository.getType(id)
        }
        _addEditTypeUIState.update {
            AddEditTypeUIState.TypeStateChanged(data)
        }

    }


}