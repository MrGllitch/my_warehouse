package eu.glitch.mywarehouse.ui.screens.record

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import eu.glitch.mywarehouse.database.IRepository
import javax.inject.Inject

@HiltViewModel
class AddEditRecordViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel(), AddEditRecordActions {

    var data: AddEditRecordData = AddEditRecordData()

    private val _addEditRecordUIState: MutableStateFlow<AddEditRecordUIState> = MutableStateFlow(value =  AddEditRecordUIState.Loading)
    val addEditProductUIState = _addEditRecordUIState.asStateFlow()

}