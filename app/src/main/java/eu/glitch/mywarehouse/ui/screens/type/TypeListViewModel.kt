package eu.glitch.mywarehouse.ui.screens.type

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import eu.glitch.mywarehouse.database.IRepository
import javax.inject.Inject

@HiltViewModel
class TypeListViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel(), TypeListActions {

    var data: TypeListData = TypeListData()

    private val _TypeListUIState: MutableStateFlow<TypeListUIState> = MutableStateFlow(value = TypeListUIState.Loading)
    val productListUIState = _TypeListUIState.asStateFlow()

}