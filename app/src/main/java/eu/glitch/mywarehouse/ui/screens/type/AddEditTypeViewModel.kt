package eu.glitch.mywarehouse.ui.screens.type

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import eu.glitch.mywarehouse.database.type.ILocalTypeRepository
import javax.inject.Inject

@HiltViewModel
class AddEditTypeViewModel @Inject constructor(
    private val typeRepository: ILocalTypeRepository
) : ViewModel(), AddEditTypeActions {

    var data: AddEditTypeData = AddEditTypeData()

    private val _addEditTypeUIState: MutableStateFlow<AddEditTypeUIState> = MutableStateFlow(value =  AddEditTypeUIState.Loading)
    val addEditProductUIState = _addEditTypeUIState.asStateFlow()

}