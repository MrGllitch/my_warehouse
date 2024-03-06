package cz.mendelu.pef.ui.screens

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
    init {
        viewModelScope.launch {
        repository.insert(Task("Prvni ukol"))
    }}
    fun loadTasks(){
        viewModelScope.launch {
        repository.getAll().collect{
          //TODO dat vedet obrazovce ze jsou nove ukoly
        }
        }
    }
}