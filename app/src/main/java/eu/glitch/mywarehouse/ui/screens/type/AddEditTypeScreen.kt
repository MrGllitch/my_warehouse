package eu.glitch.mywarehouse.ui.screens.type

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import eu.glitch.mywarehouse.ui.elements.GetIconByName
import eu.glitch.mywarehouse.ui.elements.IconPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTypeScreen(navigationRouter: INavigationRouter, id: String?) {

    val viewModel = hiltViewModel<AddEditTypeViewModel>()

    var data by remember {
        mutableStateOf(AddEditTypeData())
    }

    val state = viewModel.addEditProductUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            AddEditTypeUIState.Loading -> {
                if (id != null) {
                    viewModel.loadType(id)
                }

            }

            is AddEditTypeUIState.TypeStateChanged -> {
                data = it.data
            }

            AddEditTypeUIState.Success -> {
                LaunchedEffect(it) {
                    navigationRouter.navigateToTypeListScreen()
                }
            }
        }
    }


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = if (id == null) {
                    stringResource(R.string.add_type)
                } else {
                    stringResource(R.string.edit_type)
                }
            )
        }, navigationIcon = {
            IconButton(
                onClick = { navigationRouter.returnBack() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
            }
        })
    }) {
        AddEditProductScreenContent(
            paddingValues = it,
            data = data,
            actions = viewModel,
            navigationRouter = navigationRouter
        )
    }
}

@Composable
fun AddEditProductScreenContent(
    paddingValues: PaddingValues,
    data: AddEditTypeData,
    actions: AddEditTypeActions,
    navigationRouter: INavigationRouter
) {
    var showIconPickerDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        OutlinedTextField(value = data.type.name,
            onValueChange = { actions.nameChanged(it) },
            label = {
                Text(
                    text = stringResource(R.string.type_name)
                )
            },
            isError = data.nameError != null,
            supportingText = {
                if (data.nameError != null) {
                    Text(text = data.nameError!!)
                }
            }
        )

        Button(onClick = { showIconPickerDialog = true }) {
            Row {

                if (data.type.iconRoute != "") {
                    GetIconByName(name = data.type.iconRoute, contentDescription = "")
                } else {
                    Text(
                        text = stringResource(R.string.select_icon)
                    )
                }

            }
        }

        IconPicker(
            iconRoute = data.type.iconRoute,
            onDismiss = { showIconPickerDialog = false },
            onSelect = { actions.iconChanged(it) },
            displayed = showIconPickerDialog
        )
        Spacer(modifier = Modifier.weight(1.0f))
        AddEditRow(
            name = data.type.name,
            id = data.type.id,
            onAddEdit = { actions.addEditType() },
            onDelete = { actions.deleteType() })
    }
}


