package eu.glitch.mywarehouse.ui.screens.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.navigation.NavigationDrawer
import eu.glitch.mywarehouse.ui.elements.NoItem
import eu.glitch.mywarehouse.ui.elements.TextFieldWError
import eu.glitch.mywarehouse.ui.theme.surfaceLight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditChipScreen(navigationRouter: INavigationRouter, id: String?) {
    val viewModel = hiltViewModel<AddEditChipViewModel>()

    var data by remember {
        mutableStateOf(AddEditChipData())
    }


    val state = viewModel.addEditChipUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            AddEditChipUIState.Loading -> {
                viewModel.loadChips()
            }

            is AddEditChipUIState.ProductStateChanged -> {
                data = it.data
            }

            AddEditChipUIState.Success -> {
                LaunchedEffect(it) {
                    navigationRouter.returnBack()
                }
            }
        }
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    NavigationDrawer(drawerState = drawerState, navigationRouter = navigationRouter, content = {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.tag)
                    )
                },
                navigationIcon = {
                    val coroutineScope = rememberCoroutineScope()
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                })
        },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.newChipCreation()
                        viewModel.isAddEditOpen(true)
                    }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            }
        ) {

            AddEditChipScreenContent(
                paddingValues = it,
                data = data,
                actions = viewModel
            )

            CreateChipDialog(data = data, actions = viewModel)

        }
    })
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEditChipScreenContent(
    paddingValues: PaddingValues,
    data: AddEditChipData,
    actions: AddEditChipActions
) {
    if (data.chips.isEmpty()) {
        NoItem(paddingValues = paddingValues, text = stringResource(R.string.no_tags))
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            data.chips.forEach {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                actions.editChipChanged(it)
                                actions.isAddEditOpen(
                                    true
                                )
                            }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = it.name)
                    }


                }
            }
        }
    }
}

@Composable
fun CreateChipDialog(
    data: AddEditChipData,
    actions: AddEditChipActions,
) {


    if (data.isAddEditOpen) {
        Dialog(onDismissRequest = { actions.isAddEditOpen(false) }) {
            Column(
                modifier = Modifier
                    .background(surfaceLight)
                    .padding(20.dp)
            ) {
                TextFieldWError(
                    label = stringResource(R.string.chip_name),
                    value = data.chip.name,
                    onValueChanged = {
                        actions.nameChanged(it)
                    },
                    error = data.nameError
                )
                Row {
                    FilledTonalButton(onClick = { actions.isAddEditOpen(false) }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (data.chip.id != "") {
                        OutlinedButton(modifier = Modifier, onClick = {
                            actions.deleteChip()
                            actions.isAddEditOpen(false)
                        }) {
                            Text(text = stringResource(R.string.remove))
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Button(onClick = {
                        run {
                            if (actions.addEditChip()) {
                                actions.isAddEditOpen(false)
                            }
                        }
                    }) {
                        Text(
                            text = if (data.chip.id != "") {
                                stringResource(R.string.submit)
                            } else {
                                stringResource(R.string.add)
                            }
                        )
                    }
                }
            }

        }
    }
}