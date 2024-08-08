package eu.glitch.mywarehouse.ui.screens.type

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import eu.glitch.mywarehouse.ui.elements.GetIconByName
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeListScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<TypeListViewModel>()

    var data by remember {
        mutableStateOf(TypeListData())
    }

    val state = viewModel.productListUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            TypeListUIState.Loading -> {
                LaunchedEffect(it) {
                    viewModel.loadTypes()
                }
            }

            is TypeListUIState.TypeListChanged -> {
                data = it.data
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(drawerState = drawerState, navigationRouter = navigationRouter, content = {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.type_list)) },
                navigationIcon = {
                    val coroutineScope = rememberCoroutineScope()
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                })
        },
            floatingActionButton = {
                FloatingActionButton(onClick = { navigationRouter.navigateToAddEditTypeScreen(null) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            }) {
            ProductListScreenContent(
                paddingValues = it,
                data = data,
                actions = viewModel,
                navigationRouter = navigationRouter
            )
        }
    })
}

@Composable
fun ProductListScreenContent(
    paddingValues: PaddingValues,
    data: TypeListData,
    actions: TypeListActions,
    navigationRouter: INavigationRouter
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        data.types.forEach {
            item {
                TypeRow(iconName = it.iconRoute, name = it.name, onCLick = {
                    navigationRouter.navigateToAddEditTypeScreen(it.id)
                })
            }
        }
    }
}

@Composable
fun TypeRow(iconName: String, name: String, onCLick: () -> Unit) {
    Row(modifier = Modifier
        .clickable { onCLick() }
        .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        GetIconByName(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(28.dp),
            name = iconName,
            contentDescription = ""
        )
        Text(
            modifier = Modifier.width(230.dp),
            text = name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
    }
    HorizontalDivider()
}

