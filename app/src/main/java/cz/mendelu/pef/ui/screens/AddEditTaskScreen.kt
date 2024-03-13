package cz.mendelu.pef.ui.screens

import android.annotation.SuppressLint
import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.pef.R
import cz.mendelu.pef.navigation.Destination
import cz.mendelu.pef.navigation.INavigationRouter
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<AddEditTaskViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                                 IconButton(onClick = {navigationRouter.returnBack()}) {
                                     Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                 }
                },
                title = {
                Text(text = "Add Task")
            })
        },
    ) {
        AddEditTaskScreenContent(paddingValues = it, viewModel, navigationRouter)
    }
}

@Composable
fun AddEditTaskScreenContent(
    paddingValues: PaddingValues,
    viewModel: AddEditTaskViewModel,
    navigationRouter: INavigationRouter) {
    var taskName by rememberSaveable {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        Text(text = "Task Name")
        TextField(value = taskName, onValueChange = {taskName = it})
        Button(onClick = { coroutineScope.launch {
            viewModel.addTask(taskName)
            navigationRouter.returnBack()
            }
        }) {
         Text(text = "Add")
        }
    }
}