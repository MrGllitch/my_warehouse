package eu.glitch.mywarehouse.ui.screens.unit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditUnitScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<AddEditUnitViewModel>()

    var data by remember {
        mutableStateOf(AddEditUnitData())
    }

    val state = viewModel.addEditProductUIState.collectAsStateWithLifecycle()

    state.value.let {
        when(it){
            AddEditUnitUIState.Loading -> {

            }

            is AddEditUnitUIState.UnitStateChanges -> {
                data = it.data
            }
        }
    }


    Scaffold(topBar = { TopAppBar(title = { Text(text = "Add/Edit Unit") }) }) {
        AddEditProductScreenContent(paddingValues = it,data = data, actions =  viewModel)
    }
}

@Composable
fun AddEditProductScreenContent(
    paddingValues: PaddingValues,
    data: AddEditUnitData,
    actions: AddEditUnitActions
) {
}


