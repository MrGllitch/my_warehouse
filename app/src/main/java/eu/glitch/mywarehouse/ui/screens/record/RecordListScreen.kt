package eu.glitch.mywarehouse.ui.screens.record

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.model.RecordDetails
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.navigation.NavigationDrawer
import eu.glitch.mywarehouse.ui.elements.GetIconByName
import eu.glitch.mywarehouse.ui.elements.TextFieldWError
import eu.glitch.mywarehouse.ui.elements.shake
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
    var isEmpty = false

    state.value.let {
        when (it) {
            RecordListUIState.Loading -> {
                viewModel.loadRecordDetails()
            }

            is RecordListUIState.RecordListChanged -> {
                data = it.data
                isEmpty = it.data.records.isEmpty()
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(drawerState = drawerState, navigationRouter = navigationRouter, content = {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.record_list)) },
                navigationIcon = {
                    val coroutineScope = rememberCoroutineScope()
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                })
        },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = if (isEmpty) {
                        Modifier.shake()
                    } else {
                        Modifier
                    }, onClick = {
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
    if (data.records.isEmpty()) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "",
                modifier = Modifier.size(32.dp),
                tint = Color.Gray
            )
            Text(
                text = stringResource(R.string.please_create_record),
                modifier = Modifier.padding(top = 16.dp, bottom = 160.dp),
                fontSize = 28.sp,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            data.records.forEach {
                item {
                    RecordRow(
                        recordDetails = it,
                        actions = actions,
                        data = data,
                        onClick = { isEditVisible = true })
                }
            }
        }

        if (isEditVisible)
            EditRecord(data = data, actions = actions, onCancel = { isEditVisible = false })
    }
}

@Composable
fun RecordRow(
    recordDetails: RecordDetails,
    actions: RecordListActions,
    data: RecordListData,
    onClick: () -> Unit
) {
    val oneMonthFromToday = Calendar.getInstance().apply {
        add(Calendar.MONTH, 1)
    }.timeInMillis


    Row(modifier = Modifier
        .padding(16.dp)
        .clickable {
            Log.d("clicking", "${recordDetails.record.id} ")
            actions.setEditedRecord(recordDetails)
            data.tempQuantity = recordDetails.record.quantity.toString()

            onClick()
        }) {
        GetIconByName(name = recordDetails.type.iconRoute, contentDescription = "")
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.best_before) + " ${
                    DateUtils.getDateString(
                        recordDetails.record.dateTo
                    )
                }",
                fontSize = 12.sp,
                color = if (recordDetails.record.dateTo <= oneMonthFromToday) Color.Red else Color.Black
            )
            Text(text = recordDetails.product.name, fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Text(text = "${recordDetails.record.quantity}/${recordDetails.record.startQuantity} ${recordDetails.unit!!.shortcut}")
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
            Row(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = data.editRecord!!.product!!.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Button(onClick = {
                    actions.quantityChanged("0")
                    actions.submitChanges()
                    onCancel()
                }) {
                    Text(text = "0")
                }
            }

            TextFieldWError(
                label = stringResource(R.string.quantity),
                value = data.tempQuantity,
                onValueChanged = { actions.quantityChanged(it) },
                error = data.editRecordQuantityError,
                keyboardNumber = true,
                trailingIcon = {
                    Text(text = data.editRecord!!.unit!!.shortcut)
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
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
            Row(modifier = Modifier.padding(8.dp)) {
                Button(onClick = { onCancel() }) {
                    Text(text = stringResource(R.string.cancel))
                }
                Spacer(modifier = Modifier.weight(1.0f))
                Button(onClick = {
                    actions.submitChanges()
                    onCancel()
                }) {
                    Text(text = stringResource(R.string.submit))
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
    IconButton(modifier = Modifier.border(
        BorderStroke(width = 1.dp, color = Color.Gray)
    ), onClick = { onClick() }) {
        Text(text = value)
    }
}