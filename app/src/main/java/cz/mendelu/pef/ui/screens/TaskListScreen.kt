package cz.mendelu.pef.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.pef.R
import cz.mendelu.pef.navigation.INavigationRouter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<TaskListViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(title = { 
                Text(text = stringResource(id = R.string.app_name))
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigationRouter.navigateToAddEditTask(null) }) {

            }
        }
    ) {
        TaskListScreenContent(paddingValues = it)
    }
}

@Composable
fun TaskListScreenContent(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        Text(text = "TaskListScreenContent text")
    }
}