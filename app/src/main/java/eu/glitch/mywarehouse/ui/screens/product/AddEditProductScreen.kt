package eu.glitch.mywarehouse.ui.screens.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.ui.elements.AddEditRow
import eu.glitch.mywarehouse.ui.elements.GetIconByName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(navigationRouter: INavigationRouter, id: Long?) {

    val viewModel = hiltViewModel<AddEditProductViewModel>()

    var data by remember {
        mutableStateOf(AddEditProductData())
    }

    val state = viewModel.addEditProductUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            AddEditProductUIState.Loading -> {
                if (id != null) {
                    viewModel.loadProduct(id)
                }
                viewModel.loadTypes()
            }

            is AddEditProductUIState.ProductStateChanged -> {
                data = it.data
            }

            AddEditProductUIState.Success -> {
                LaunchedEffect(it) {
                    navigationRouter.returnBack()
                }
            }
        }
    }


    Scaffold(topBar = { TopAppBar(title = { Text(text = "Add/Edit Product") }) }) {
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
    data: AddEditProductData,
    actions: AddEditProductActions,
    navigationRouter: INavigationRouter,

    ) {

    var isItemTypeOpen by remember { mutableStateOf(false) }
    var selectedItem =
        data.types.find { type -> type.id == data.product.typeId } ?: Type(null, "", "")

    Column(modifier = Modifier.padding(paddingValues)) {
        OutlinedTextField(value = data.product.name,
            onValueChange = { actions.nameChanged(it) },
            label = {
                Text(
                    text = "Jmeno produktu"
                )
            },
            isError = data.nameError != null,
            supportingText = {
                if (data.nameError != null) {
                    Text(text = data.nameError!!)
                }
            }
        )
        ExposedDropdownMenuBox(
            expanded = isItemTypeOpen,
            onExpandedChange = { isItemTypeOpen = !isItemTypeOpen }) {

            TextField(
                value = selectedItem.name,
                leadingIcon = {
                    if (selectedItem.iconRoute != "") {
                        GetIconByName(
                            name = selectedItem.iconRoute,
                            contentDescription = ""
                        )
                    } else {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    }
                },
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isItemTypeOpen) },
                modifier = Modifier.menuAnchor(),
                isError = data.typeError != null,
                supportingText = {
                    if (data.typeError != null) {
                        Text(text = data.typeError!!)
                    }
                }
            )


            ExposedDropdownMenu(
                expanded = isItemTypeOpen,
                onDismissRequest = { isItemTypeOpen = false }) {
                data.types.forEach {
                    DropdownMenuItem(leadingIcon = {
                        GetIconByName(
                            name = it.iconRoute,
                            contentDescription = ""
                        )
                    }, text = { Text(text = it.name) }, onClick = {
                        actions.typeChanged(it.id!!)
                        isItemTypeOpen = false
                    })
                }
            }
        }


        Spacer(modifier = Modifier.weight(1.0f))
        AddEditRow(
            name = data.product.name,
            id = data.product.id,
            onAddEdit = { actions.addEditProduct() },
            onCancel = { navigationRouter.returnBack() }) {

        }
    }
}


