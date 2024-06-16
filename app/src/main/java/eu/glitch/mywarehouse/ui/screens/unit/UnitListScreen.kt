package eu.glitch.mywarehouse.ui.screens.unit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.navigation.NavigationDrawer
import eu.glitch.mywarehouse.ui.theme.displayFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitListScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<UnitListViewModel>()

    var data by remember {
        mutableStateOf(UnitListData())
    }

    val state = viewModel.unitListUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            UnitListUIState.Loading -> {
                LaunchedEffect(it) {
                    viewModel.loadUnits()
                }
            }

            is UnitListUIState.UnitStateChanged -> {
                data = it.data
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(drawerState = drawerState, navigationRouter = navigationRouter, content = {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.unit_list)) },
                navigationIcon = {
                    val coroutineScope = rememberCoroutineScope()
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                })
        },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navigationRouter.navigateToAddEditUnitScreen(null)
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            }
        ) {
            ProductListScreenContent(
                paddingValues = it,
                data = data,
                actions = viewModel,
                navigationRouter = navigationRouter
            )
        }
    }
    )
}


@Composable
fun ProductListScreenContent(
    paddingValues: PaddingValues,
    data: UnitListData,
    actions: UnitListActions,
    navigationRouter: INavigationRouter
) {
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        data.units.forEach {
            item {
                UnitRow(
                    onClick = { navigationRouter.navigateToAddEditUnitScreen(it.id) },
                    shortCut = it.shortcut,
                    name = it.name
                )
            }
        }

    }
}


@Composable
fun UnitRow(
    onClick: () -> Unit,
    shortCut: String,
    name: String
) {
    Row(
        modifier = Modifier

            .clickable(onClick = { onClick() })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = shortCut,
                fontFamily = displayFontFamily, fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.width(230.dp),
                text = name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Light,
                maxLines = 3
            )
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")

    }
    HorizontalDivider()
}


