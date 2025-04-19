package eu.glitch.mywarehouse.ui.screens.type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.database.impl.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeListViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel(), TypeListActions {

    var data: TypeListData = TypeListData()

    private val _typeListUIState: MutableStateFlow<TypeListUIState> =
        MutableStateFlow(value = TypeListUIState.Loading)
    val productListUIState = _typeListUIState.asStateFlow()

    override fun loadTypes() {
        viewModelScope.launch {
            data.isAdmin = repository.isAdmin()
            data.types = repository.getTypes().toMutableList()
                _typeListUIState.update {
                    TypeListUIState.TypeListChanged(data)
                }


        }
    }

}