package eu.glitch.mywarehouse.ui.screens.record

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.extensions.getValue
import eu.glitch.mywarehouse.model.Barcode
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.ui.elements.AddEditRow
import eu.glitch.mywarehouse.ui.theme.primaryLight
import eu.glitch.mywarehouse.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecordScreen(navigationRouter: INavigationRouter, id: String?) {

    val viewModel = hiltViewModel<AddEditRecordViewModel>()

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        val barcode = navigationRouter.getNavController().getValue<String>("barcode")
        barcode?.value?.let {
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Barcode> = moshi.adapter(Barcode::class.java)
            val result = jsonAdapter.fromJson(it)
            result?.let {
                Log.d("barcode", "returned with barcode ${it.code} ")
                viewModel.barcodePrepopulate(it.code)
            }
        }
    }

    var data by remember {
        mutableStateOf(AddEditRecordData())
    }

    val state = viewModel.addEditProductUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            AddEditRecordUIState.Loading -> {
                if (id != null) {
                    viewModel.loadRecord(id)
                }
                viewModel.loadUnits()
                viewModel.loadProducts()
                viewModel.loadTypes()
            }

            is AddEditRecordUIState.RecordDataChanged -> {
                data = it.data
            }

            AddEditRecordUIState.Success -> {
                LaunchedEffect(it) {
                    navigationRouter.navigateToRecordListScreen()
                }
            }
        }
    }


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = if (id == null) {
                    stringResource(R.string.add_record)
                } else {
                    stringResource(R.string.edit_record)
                }
            )
        }, navigationIcon = {
            IconButton(
                onClick = { navigationRouter.returnBack() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
            }
        },
            actions = {
                IconButton(onClick = { navigationRouter.navigateToBarcodeScannerScreen() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_barcode),
                        contentDescription = ""
                    )
                }
            })
    }) {
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
    data: AddEditRecordData,
    actions: AddEditRecordActions,
    navigationRouter: INavigationRouter
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        var isProductExpanded by remember {
            mutableStateOf(false)
        }
        val selectedProduct = data.products.find {
            it.id == (data.record.productId)
        }
        val unit = data.units.find {
            it.id == selectedProduct?.unitId
        }



        Column {
            Row(modifier = Modifier.clickable { isProductExpanded = !isProductExpanded }) {

                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = selectedProduct?.name ?: stringResource(R.string.select_product),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                if (isProductExpanded) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "",
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "",
                        modifier = Modifier.size(32.dp)
                    )
                }


            }

            DropdownMenu(
                expanded = isProductExpanded,
                onDismissRequest = { isProductExpanded = false }) {
                data.products.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        //leadingIcon = GetIconByName(name = it.typeId, contentDescription = ""),
                        onClick = {
                            actions.productChanged(it.id.toString())
                            isProductExpanded = false
                        })

                }

            }
        }
        if (data.productError != null) {
            Text(text = data.productError!!, color = Color.Red)
        }



        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = data.record.quantity.toString(),
            onValueChange = { actions.quantityChanged(it) },
            label = { Text(text = stringResource(R.string.quantity)) },
            trailingIcon = {
                if (unit != null) {
                    Text(text = unit.shortcut)
                }
            },
            isError = data.quantityError != null,
            supportingText = {
                if (data.quantityError != null) {
                    Text(text = data.quantityError!!)
                }
            })

        Slider(
            modifier = Modifier.padding(horizontal = 30.dp),
            value = data.record.quantity.toFloat(),
            onValueChange = { actions.quantityChanged(it.toInt().toString()) },
            valueRange = 0f..100f,
            steps = 99
        )

        val state =
            rememberDatePickerState(
                initialSelectedDateMillis = System.currentTimeMillis(),
                initialDisplayMode = DisplayMode.Input
            )

        LaunchedEffect(state.selectedDateMillis) {
            state.selectedDateMillis?.let { selectedDate ->
                actions.dateToChanged(selectedDate.toString())
            }
        }

        LazyColumn(
            modifier = Modifier
                .height(500.dp)
                .padding(8.dp)

        ) {
            item {
                DatePicker(
                    modifier = Modifier,
                    state = state,
                    title = {
                        Text(
                            stringResource(R.string.best_before),
                            fontSize = 28.sp,
                            color = primaryLight
                        )
                    },
                    headline = { Text(text = DateUtils.getDateString(data.record.dateTo)) },
                )
            }
        }








        Spacer(modifier = Modifier.weight(1f))
        AddEditRow(
            name = stringResource(R.string.record),
            id = data.record.id,
            onAddEdit = { actions.addEditRecord() },
            onDelete = { })
    }


}
