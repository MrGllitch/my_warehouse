package eu.glitch.mywarehouse.ui.screens.unit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditUnitScreen(navigationRouter: INavigationRouter, id: Long?) {

    val viewModel = hiltViewModel<AddEditUnitViewModel>()

    var data by remember {
        mutableStateOf(AddEditUnitData())
    }

    val state = viewModel.addEditProductUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            AddEditUnitUIState.Loading -> {
                if (id != null) {
                    viewModel.loadUnit(id)
                }

            }

            is AddEditUnitUIState.UnitStateChanged -> {
                data = it.data
            }

            AddEditUnitUIState.Success -> {
                LaunchedEffect(it) {
                    navigationRouter.navigateToUnitListScreen()

                }
            }
        }
    }


    Scaffold(topBar = { TopAppBar(title = { Text(text = "Add/Edit Unit") }) }) {
        AddEditProductScreenContent(paddingValues = it, data = data, actions = viewModel)
    }
}

@Composable
fun AddEditProductScreenContent(
    paddingValues: PaddingValues,
    data: AddEditUnitData,
    actions: AddEditUnitActions
) {
    Column(modifier = Modifier.padding(paddingValues)) {

        OutlinedTextField(value = data.unit.name,
            onValueChange = { actions.nameChanged(it) },
            label = {
                Text(
                    text = "Jmeno jednotky"
                )
            },
            isError = data.nameError != null,
            supportingText = {
                if (data.nameError != null) {
                    Text(text = data.nameError!!)
                }
            }
        )

        OutlinedTextField(value = data.unit.shortcut,
            onValueChange = { actions.shortCutChanged(it) },
            label = {
                Text(
                    text = "Zkratka jednotky"
                )
            },
            isError = data.unitError != null,
            supportingText = {
                if (data.unitError != null) {
                    Text(text = data.unitError!!)
                }
            }
        )
        Row {
            Button(onClick = { actions.addUnit() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
                Text(text = "Pridat")
            }
        }
    }
}


