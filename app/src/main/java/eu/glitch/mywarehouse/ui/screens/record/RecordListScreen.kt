package eu.glitch.mywarehouse.ui.screens.record

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
import androidx.compose.material3.ButtonColors
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import eu.glitch.mywarehouse.model.RecordListItem
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.navigation.NavigationDrawer
import eu.glitch.mywarehouse.ui.elements.FilterRow
import eu.glitch.mywarehouse.ui.elements.GetIconByName
import eu.glitch.mywarehouse.ui.elements.NoItem
import eu.glitch.mywarehouse.ui.elements.TextFieldWError
import eu.glitch.mywarehouse.ui.elements.shake
import eu.glitch.mywarehouse.ui.theme.errorContainerLight
import eu.glitch.mywarehouse.ui.theme.errorLight
import eu.glitch.mywarehouse.ui.theme.onPrimaryContainerLight
import eu.glitch.mywarehouse.ui.theme.primaryLight
import eu.glitch.mywarehouse.ui.theme.secondaryContainerLight
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


    LaunchedEffect(Unit) {
        viewModel.loadRecords()
        viewModel.loadTypes()
        viewModel.loadChips()
    }

    state.value.let {
        when (it) {
            is RecordListUIState.Loading -> {
                // viewModel.loadRecords()
                // viewModel.loadTypes()
                // viewModel.loadChips()
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
        NoItem(paddingValues = paddingValues, text = stringResource(R.string.please_create_record))
    } else {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                FilterRow(
                    items = data.types,
                    type = "typeId",
                    onClick = { type, id, selected ->
                        actions.editFilter(type, id, selected)
                    },
                )
            }
            item {
                FilterRow(
                    items = data.chips,
                    type = "chipIds",
                    onClick = { type, id, selected ->
                        actions.editFilter(type, id, selected)
                    },
                )
            }
            data.filteredRecords.forEach {
                item {
                    RecordRow(
                        item = it,
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
    item: RecordListItem,
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
            actions.setEditedRecord(item)
            data.tempQuantityChange = (item.record.quantity * -1).toString()

            onClick()
        }) {
        GetIconByName(name = item.iconRoute, contentDescription = "")
        Column(verticalArrangement = Arrangement.Center) {
            Row {
                Text(
                    text = stringResource(R.string.best_before) + " ",
                    fontSize = 12.sp,
                    color = primaryLight
                )
                Text(
                    text = DateUtils.getDateString(
                        item.record.dateTo
                    ),
                    fontSize = 12.sp,
                    fontWeight = if (item.record.dateTo <= oneMonthFromToday) FontWeight.Bold else FontWeight.Medium,
                    color = if (item.record.dateTo <= oneMonthFromToday) errorLight else primaryLight
                )
            }
            Text(text = item.name, fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Text(text = "${item.record.quantity}/${item.record.startQuantity} ${item.shortcut}")
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
                    text = data.editRecordName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1.0f))
            }

            TextFieldWError(
                label = stringResource(R.string.quantity),
                value = data.tempQuantityChange,
                onValueChanged = { actions.quantityChanged(it) },
                error = data.editRecordQuantityError,
                keyboardNumber = true,
                trailingIcon = {
                    Text(text = data.editRecordShortcut)
                },
                prefix = {
                    Text(
                        text = data.editRecord!!.quantity.toString(),
                        color = primaryLight,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
            Row(modifier = Modifier.padding(8.dp)) {
                Button(
                    colors = ButtonColors(
                        secondaryContainerLight,
                        onPrimaryContainerLight,
                        errorContainerLight,
                        errorContainerLight
                    ), onClick = { onCancel() }) {
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