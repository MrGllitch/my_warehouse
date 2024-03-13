package cz.mendelu.pef.ui.screens

import androidx.lifecycle.ViewModel
import cz.mendelu.pef.database.ILocalTasksRepository
import cz.mendelu.pef.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(private val repository: ILocalTasksRepository) : ViewModel(){

        suspend fun addTask(name: String){
            repository.insert(Task(name))
        }
    }