package eu.glitch.mywarehouse.ui.screens.unit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import eu.glitch.mywarehouse.database.IRepository
import javax.inject.Inject

@HiltViewModel
class UnitListViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel(), UnitListActions {

    var data: UnitListData = UnitListData()

    private val _UnitListUIState: MutableStateFlow<UnitListUIState> = MutableStateFlow(value = UnitListUIState.Loading)
    val productListUIState = _UnitListUIState.asStateFlow()

}