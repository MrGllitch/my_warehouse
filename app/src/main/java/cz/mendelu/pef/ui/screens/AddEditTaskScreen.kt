package cz.mendelu.pef.ui.screens

import android.annotation.SuppressLint
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
import cz.mendelu.pef.R
import cz.mendelu.pef.navigation.Destination


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { 
                Text(text = "Add Task :]")
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {

            }
        }
    ) {
        AddEditTaskScreenContent(paddingValues = it)
    }
}

@Composable
fun AddEditTaskScreenContent(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        Text(text = "TaskListScreenContent text")
    }
}