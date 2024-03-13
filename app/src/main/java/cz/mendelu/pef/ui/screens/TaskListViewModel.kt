package cz.mendelu.pef.ui.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.pef.database.ILocalTasksRepository
import cz.mendelu.pef.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val repository: ILocalTasksRepository) : ViewModel() {

    val taskListUIState: MutableState<TaskListUIState> =
        mutableStateOf(TaskListUIState.Loading())
        /*
    init {
        viewModelScope.launch {
        for (i in 1..50)
            repository.insert(Task("Prvni ukol")
        )
    }}*/
    fun loadTasks(){
        viewModelScope.launch {
        repository.getAll().collect{
          taskListUIState.value = TaskListUIState.Success(it)
            }
        }
    }
}