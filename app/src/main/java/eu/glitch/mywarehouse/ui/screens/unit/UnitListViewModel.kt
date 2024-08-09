package eu.glitch.mywarehouse.ui.screens.unit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.Repository
import eu.glitch.mywarehouse.database.unit.ILocalUnitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnitListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(), UnitListActions {

    var data: UnitListData = UnitListData()

    private val _unitListUIState: MutableStateFlow<UnitListUIState> =
        MutableStateFlow(value = UnitListUIState.Loading)
    val unitListUIState = _unitListUIState.asStateFlow()

    override fun loadUnits() {
        viewModelScope.launch {
            data.units = repository.getUnits().toMutableList()
                _unitListUIState.update {
                    UnitListUIState.UnitStateChanged(data)
                }
            }

        }

    }