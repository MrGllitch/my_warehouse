package eu.glitch.mywarehouse.ui.screens.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.ui.elements.AddEditRow
import eu.glitch.mywarehouse.utils.DateUtils

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
                LaunchedEffect(it) {
                    navigationRouter.returnBack()
                }
            }
        }
    }


    Scaffold(topBar = { TopAppBar(title = { Text(text = "Add/Edit Record") }) }) {
        AddEditProductScreenContent(
            paddingValues = it,
            data = data,
            actions = viewModel,
            navigationRouter = navigationRouter
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreenContent(
    paddingValues: PaddingValues,
    data: AddEditRecordData,
    actions: AddEditRecordActions,
    navigationRouter: INavigationRouter
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        var isProductExpanded by remember {
            mutableStateOf(false)
        }
        val selectedProduct = data.products.find {
            it.id.toString() == (data.record.productId ?: null)
        }
        var isUnitExpanded by remember {
            mutableStateOf(false)
        }
        val selectedUnit = data.units.find {
            it.id.toString() == (data.record.unitId ?: null)
        }

        Column {
            Text(
                modifier = Modifier
                    .clickable { isProductExpanded = !isProductExpanded }
                    .padding(16.dp),
                text = selectedProduct?.name ?: "Select Product"
            )
            DropdownMenu(
                expanded = isProductExpanded,
                onDismissRequest = { isProductExpanded = false }) {
                data.products.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        //leadingIcon = GetIconByName(name = it.typeId, contentDescription = ""),
                        onClick = {
                            actions.productChanged(it.id.toString())
                            isProductExpanded = false
                        })

                }

            }
        }
        if (data.productError != null) {
            Text(text = data.productError!!, color = Color.Red)
        }

        Row {
            OutlinedTextField(
                value = data.record.quantity,
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
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Column {
                Row(modifier = Modifier.clickable { isUnitExpanded = !isUnitExpanded }) {
                    Text(text = selectedUnit?.name ?: "Jednotka")
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                }
                if (data.unitError != null) {
                    Text(text = data.unitError!!, color = Color.Red)
                }

            }
            DropdownMenu(
                expanded = isUnitExpanded,
                onDismissRequest = { isUnitExpanded = false }) {
                data.units.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        onClick = {
                            actions.unitChanged(it.id.toString())
                            isUnitExpanded = false
                        })

                }

            }
        }
        Slider(
            value = data.record.quantity.toFloat(),
            onValueChange = { actions.quantityChanged(it.toInt().toString()) },
            valueRange = 0f..100f,
            steps = 99
        )

        val state =
            rememberDatePickerState(initialSelectedDateMillis = data.record.dateTo.toLongOrNull())
        var isDatePickerVisible by remember {
            mutableStateOf(false)
        }

        Row(modifier = Modifier.clickable { isDatePickerVisible = !isDatePickerVisible }) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "")
            if (data.record.dateTo != "") {
                Text(text = DateUtils.getDateString(data.record.dateTo.toLong()))
            } else {
                Text(text = "Select a date")
            }
            if (data.dateError != null) {
                Text(text = data.dateError!!, color = Color.Red)
            }
        }

        if (isDatePickerVisible)
            DatePickerDialog(
                onDismissRequest = {
                    isDatePickerVisible = false
                },
                confirmButton = {
                    Button(onClick = {
                        if (state.selectedDateMillis != null) {
                            actions.dateToChanged(state.selectedDateMillis!!.toString())
                            isDatePickerVisible = false
                        }
                    }) {
                        Text(text = "Select")
                    }
                },
                dismissButton = {
                    Button(onClick = { isDatePickerVisible = false }) {
                        Text(text = "Cancel")
                    }
                }


            ) {
                DatePicker(state = state)
            }


        Spacer(modifier = Modifier.weight(1.0f))
        AddEditRow(
            name = "Record",
            id = data.record.id,
            onAddEdit = { actions.addEditRecord() },
            onCancel = { navigationRouter.returnBack() },
            onDelete = { actions.deleteRecord() })
    }


}
