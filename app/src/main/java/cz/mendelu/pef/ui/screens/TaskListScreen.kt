package cz.mendelu.pef.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.pef.R
import cz.mendelu.pef.model.Task
import cz.mendelu.pef.navigation.INavigationRouter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<TaskListViewModel>()

    val tasks: MutableList<Task> = mutableListOf()

    viewModel.taskListUIState.value.let {

        when(it){
            is TaskListUIState.Loading -> {
                viewModel.loadTasks()
            }
            is TaskListUIState.Success -> {
                tasks.addAll(it.tasks)
            }
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(title = { 
                Text(text = stringResource(id = R.string.app_name))
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigationRouter.navigateToAddEditTask(null) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Goto add task screen")
            }
        }
    ) {
        TaskListScreenContent(paddingValues = it, tasks = tasks)
    }
}

@Composable
fun TaskListScreenContent(
    paddingValues: PaddingValues,
    tasks: List<Task>) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        if (tasks.size == 0 ){
            item { Text(text = "Dont have any tasks :{") }
        }else{
            tasks.forEach{
                item{
            TaskListRow(task = it)
            }
        }}
    }
}

@Composable
fun TaskListRow(task: Task){
    Row {
        Text(text = task.id.toString() + " ")
        Text(text = task.text)
    }
}