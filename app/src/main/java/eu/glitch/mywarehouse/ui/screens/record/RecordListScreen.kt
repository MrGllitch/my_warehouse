package eu.glitch.mywarehouse.ui.screens.record

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.navigation.NavigationDrawer
import eu.glitch.mywarehouse.ui.elements.TextFieldWError
import eu.glitch.mywarehouse.utils.DateUtils
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordListScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<RecordListViewModel>()

    var data by remember {
        mutableStateOf(RecordListData())
    }

    val state = viewModel.recordListUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            RecordListUIState.Loading -> {
                viewModel.loadRecords()
                viewModel.loadProducts()
                viewModel.loadUnits()
            }

            is RecordListUIState.RecordListChanged -> {
                data = it.data
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(drawerState = drawerState, navigationRouter = navigationRouter, content = {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = "Record List") },
                navigationIcon = {
                    val coroutineScope = rememberCoroutineScope()
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                })
        },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navigationRouter.navigateToAddEditRecordScreen(
                        null
                    )
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            }) {
            ProductListScreenContent(paddingValues = it, data = data, actions = viewModel)
        }
    })
}

@Composable
fun ProductListScreenContent(
    paddingValues: PaddingValues,
    data: RecordListData,
    actions: RecordListActions
) {
    var isEditVisible by remember {
        mutableStateOf(false)
    }
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        data.records.forEach {
            item {
                RecordRow(
                    record = it,
                    actions = actions,
                    data = data,
                    onClick = { isEditVisible = true })
            }
        }
    }

    if (isEditVisible)
        EditRecord(data = data, actions = actions, onCancel = { isEditVisible = false })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordRow(
    record: Record,
    actions: RecordListActions,
    data: RecordListData,
    onClick: () -> Unit
) {
    val product = data.products.find { it.id == record.productId }
    val unit = data.units.find { it.id == record.unitId }
    val oneMonthFromToday = Calendar.getInstance().apply {
        add(Calendar.MONTH, 1)
    }.timeInMillis


    Row(modifier = Modifier
        .padding(16.dp)
        .clickable {
            actions.setEditedRecord(record)
            data.tempQuantity = record.quantity.toString()
            data.editName = product!!.name
            data.unit = unit!!.name
            onClick()
        }) {
        //GetIconByName(name = product.icon, contentDescription = )
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Trvanlivost do ${DateUtils.getDateString(record.dateTo)}",
                fontSize = 12.sp,
                color = if (record.dateTo <= oneMonthFromToday) Color.Red else Color.Black
            )
            Text(text = product!!.name, fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Text(text = "${record.quantity}/${record.startQuantity}")
    }
    HorizontalDivider()
}

@Composable
fun EditRecord(
    data: RecordListData,
    actions: RecordListActions,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = { onCancel() }) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row {
                Text(text = data.editName)
                Button(onClick = {
                    actions.quantityChanged("0")
                    actions.submitChanges()
                    onCancel()
                }) {
                    Text(text = "vyskladnit vse")
                }
            }

            TextFieldWError(
                label = "quantity",
                value = data.tempQuantity,
                onValueChanged = { actions.quantityChanged(it) },
                error = data.editRecordQuantityError,
                keyboardNumber = true,
                trailingIcon = {
                    //Text(text = )
                }
            )
            Row {
                EditRecordButton(
                    value = "-1",
                    onClick = { actions.quantityChanged((data.tempQuantity.toInt() - 1).toString()) })
                EditRecordButton(
                    value = "-2",
                    onClick = { actions.quantityChanged((data.tempQuantity.toInt() - 2).toString()) })
                EditRecordButton(
                    value = "-5",
                    onClick = { actions.quantityChanged((data.tempQuantity.toInt() - 5).toString()) })
                EditRecordButton(
                    value = "-10",
                    onClick = { actions.quantityChanged((data.tempQuantity.toInt() - 10).toString()) })
                EditRecordButton(
                    value = "-50",
                    onClick = { actions.quantityChanged((data.tempQuantity.toInt() - 50).toString()) })
            }
            Row {
                Button(onClick = { onCancel() }) {
                    Text(text = "cancel")
                }
                Button(onClick = {
                    actions.submitChanges()
                    onCancel()
                }) {
                    Text(text = "Submit")
                }
            }
        }
    }

}

@Composable
fun EditRecordButton(
    value: String,
    onClick: () -> Unit,

    ) {
    Button(onClick = { onClick() }) {
        Text(text = value)
    }
}