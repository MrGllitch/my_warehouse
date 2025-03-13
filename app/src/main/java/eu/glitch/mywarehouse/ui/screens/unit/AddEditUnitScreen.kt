package eu.glitch.mywarehouse.ui.screens.unit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.ui.elements.AddEditRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditUnitScreen(navigationRouter: INavigationRouter, id: String?) {

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


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = if (id == null) {
                    stringResource(R.string.add_unit)
                } else {
                    stringResource(R.string.edit_unit)
                }
            )
        }, navigationIcon = {
            IconButton(onClick = { navigationRouter.returnBack() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
            }
        })
    }) {
        AddEditProductScreenContent(paddingValues = it, data = data, actions = viewModel)
    }
}

@Composable
fun AddEditProductScreenContent(
    paddingValues: PaddingValues, data: AddEditUnitData, actions: AddEditUnitActions
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {

        OutlinedTextField(value = data.unit.name,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { actions.nameChanged(it) },
            label = {
                Text(
                    text = stringResource(R.string.unit_name)
                )
            },
            isError = data.nameError != null,
            supportingText = {
                if (data.nameError != null) {
                    Text(text = data.nameError!!)
                }
            },
            singleLine = true
            )

        OutlinedTextField(value = data.unit.shortcut,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { actions.shortCutChanged(it) },
            label = {
                Text(
                    text = stringResource(R.string.unit_shortcut)
                )
            },
            isError = data.unitError != null,
            supportingText = {
                if (data.unitError != null) {
                    Text(text = data.unitError!!)
                }
            },
            singleLine = true)
        AddEditRow(name = data.unit.name, id = data.unit.id,
            onAddEdit = { actions.addEditUnit() }, onDelete = { actions.deleteUnit() })
    }
}


