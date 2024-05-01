package eu.glitch.mywarehouse.ui.screens.unit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import eu.glitch.mywarehouse.database.IRepository
import javax.inject.Inject

@HiltViewModel
class AddEditUnitViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel(), AddEditUnitActions {

    var data: AddEditUnitData = AddEditUnitData()

    private val _addEditProductUIState: MutableStateFlow<AddEditUnitUIState> = MutableStateFlow(value =  AddEditUnitUIState.Loading)
    val addEditProductUIState = _addEditProductUIState.asStateFlow()

}