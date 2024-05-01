package eu.glitch.mywarehouse.ui.screens.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.navigation.NavigationDrawer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<ProductListViewModel>()

    var data by remember {
        mutableStateOf(ProductListData())
    }

    val state = viewModel.productListUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            ProductListUIState.Loading -> {

            }

            is ProductListUIState.ProductStateChangesList -> {
                data = it.data
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(drawerState = drawerState, navigationRouter = navigationRouter ,content = {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = "Product List") },
                navigationIcon = {
                    val coroutineScope = rememberCoroutineScope()
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                })
        }) {
            ProductListScreenContent(
                paddingValues = it,
                data = data,
                actions = viewModel,
                router = navigationRouter
            )
        }
    })
}


@Composable
fun ProductListScreenContent(
    paddingValues: PaddingValues,
    data: ProductListData,
    actions: ProductListActions,
    router: INavigationRouter
) {
    Column {
        Button(onClick = { router.navigateToAddEditProductScreen() }) {
            Text(text = "a/e product")
        }
        Button(onClick = { router.navigateToRecordListScreen() }) {
            Text(text = "record list")
        }
        Button(onClick = { router.navigateToAddEditRecordScreen() }) {
            Text(text = "a/e record")
        }
        Button(onClick = { router.navigateToTypeListScreen() }) {
            Text(text = "type list")
        }
        Button(onClick = { router.navigateToAddEditTypeScreen() }) {
            Text(text = "a/e type")
        }
        Button(onClick = { router.navigateToAddEditUnitScreen() }) {
            Text(text = "a/e unit")
        }
        Button(onClick = { router.navigateToUnitListScreen() }) {
            Text(text = "unit list")
        }
    }
}


