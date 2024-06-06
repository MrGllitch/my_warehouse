package eu.glitch.mywarehouse.ui.screens.record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
fun AddEditRecordScreen(navigationRouter: INavigationRouter, id: Long?) {

    val viewModel = hiltViewModel<AddEditRecordViewModel>()

    var data by remember {
        mutableStateOf(AddEditRecordData())
    }

    val state = viewModel.addEditProductUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            AddEditRecordUIState.Loading -> {
                if (id != null) {
                    viewModel.loadRecord(id)
                }
                viewModel.loadUnits()
                viewModel.loadProducts()
            }

            is AddEditRecordUIState.RecordDataChanged -> {
                data = it.data
            }

            AddEditRecordUIState.Success -> {

            }
        }
    }


    Scaffold(topBar = { TopAppBar(title = { Text(text = "Add/Edit Record") }) }) {
        AddEditProductScreenContent(paddingValues = it, data = data, actions = viewModel)
    }
}

@Composable
fun AddEditProductScreenContent(
    paddingValues: PaddingValues,
    data: AddEditRecordData,
    actions: AddEditRecordActions
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        //todo product selector

        OutlinedTextField(value = data.record.quantity,
            onValueChange = { actions.quantityChanged(it) },
            label = {
                Text(
                    text = "Mnozstvi"
                )
            },
            isError = data.quantityError != null,
            supportingText = {
                if (data.quantityError != null) {
                    Text(text = data.quantityError!!)
                }
            }
        )
        Slider(
            value = data.record.quantity.toFloat(),
            onValueChange = { actions.quantityChanged(it.toString()) })

        //todo date picker
    }
}


