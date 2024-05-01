package eu.glitch.mywarehouse.ui.screens.record

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordListScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<RecordListViewModel>()

    var data by remember {
        mutableStateOf(RecordListData())
    }

    val state = viewModel.productListUIState.collectAsStateWithLifecycle()

    state.value.let {
        when(it){
            RecordListUIState.Loading -> {

            }

            is RecordListUIState.RecordStateChangesList -> {
                data = it.data
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(drawerState = drawerState, navigationRouter = navigationRouter ,content = {
    Scaffold(topBar = { TopAppBar(title = { Text(text = "Record List") },
        navigationIcon = {
            val coroutineScope = rememberCoroutineScope()
            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
            }
        })},) {
        ProductListScreenContent(paddingValues = it,data = data, actions =  viewModel)
    }
})
}
@Composable
fun ProductListScreenContent(
    paddingValues: PaddingValues,
    data: RecordListData,
    actions: RecordListActions
) {
}


