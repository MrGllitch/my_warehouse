package eu.glitch.mywarehouse.ui.screens.type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.impl.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTypeViewModel @Inject constructor(
    private val repository: RepositoryImpl
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
        if (text.length < 32) {
            data.type.name = text
        }
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
        viewModelScope.launch {

            if (data.type.id == "") {
                repository.insertType(data.type)
            } else {
                repository.updateType(type = data.type)
            }

            _addEditTypeUIState.update {
                AddEditTypeUIState.Success
            }
        }
    }

    override fun deleteType() {
        viewModelScope.launch {
            repository.deleteType(data.type)
            _addEditTypeUIState.update {
                AddEditTypeUIState.Success
            }
        }

    }


    fun loadType(id: String) {
        viewModelScope.launch {
            data.type = repository.getType(id)
            _addEditTypeUIState.update {
                AddEditTypeUIState.TypeStateChanged(data)
            }
        }

    }


}