package cz.mendelu.pef.ui.screens

import cz.mendelu.pef.model.Task

sealed class TaskListUIState {
    class Loading : TaskListUIState()
    class Success(val tasks: List<Task>) : TaskListUIState()

}