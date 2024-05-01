package eu.glitch.mywarehouse.ui.screens.record

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import eu.glitch.mywarehouse.database.IRepository
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel(), RecordListActions {

    var data: RecordListData = RecordListData()

    private val _RecordListUIState: MutableStateFlow<RecordListUIState> = MutableStateFlow(value = RecordListUIState.Loading)
    val productListUIState = _RecordListUIState.asStateFlow()

}