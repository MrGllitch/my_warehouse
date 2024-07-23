package eu.glitch.mywarehouse.ui.screens.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.extensions.getValue
import eu.glitch.mywarehouse.model.Barcode
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.ui.elements.AddEditRow
import eu.glitch.mywarehouse.ui.elements.CameraView
import eu.glitch.mywarehouse.ui.elements.GetIconByName
import eu.glitch.mywarehouse.ui.elements.TextFieldWError
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(navigationRouter: INavigationRouter, id: Long?) {
    val viewModel = hiltViewModel<AddEditProductViewModel>()

    var data by remember {
        mutableStateOf(AddEditProductData())
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        val barcode = navigationRouter.getNavController().getValue<String>("barcode")
        barcode?.value?.let {
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Barcode> = moshi.adapter(Barcode::class.java)
            val result = jsonAdapter.fromJson(it)
            result?.let {
                viewModel.barcodeChanged(it.code)
            }
        }
    }


    val state = viewModel.addEditProductUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            AddEditProductUIState.Loading -> {
                if (id != null) {
                    viewModel.loadProduct(id)
                }
                viewModel.loadTypes()
                viewModel.loadUnits()
                viewModel.loadChips()
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

    var isCameraVisible by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = if (id == null) {
                    stringResource(R.string.add_product)
                } else {
                    stringResource(R.string.edit_product)
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
                    GetIconByName(name = "icon_barcode", contentDescription = "")
                }
                IconButton(onClick = { isCameraVisible = true }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_photo_camera),
                        contentDescription = "Open camera"
                    )
                }
            })
    }
    ) {
        if (isCameraVisible) {
            CameraView(
                takePhoto = { viewModel.takePhoto(it) },
                onDismissRequest = { isCameraVisible = false })
        }
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
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = data.product.name,
            onValueChange = { actions.nameChanged(it) },
            label = {
                Text(
                    text = stringResource(R.string.product_name)
                )
            },
            isError = data.nameError != null,
            supportingText = {
                if (data.nameError != null) {
                    Text(text = data.nameError!!)
                }
            }
        )

        ChipRow(actions = actions, data = data)



        Row {
            if (data.imageName == "") {
                Image(
                    painter = painterResource(id = R.drawable.image_placeholder),
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp, 150.dp)
                )
            } else {
                Box(modifier = Modifier.size(150.dp, 150.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(File("${context.filesDir}/${data.imageName}")),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp, 150.dp)
                            .clip(RectangleShape)

                    )
                    IconButton(
                        onClick = { actions.removePhoto() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                        )
                    }
                }
            }


        }

        Row {
            TypeColumn(
                modifier = Modifier.fillMaxWidth(0.5f),
                types = data.types,
                actions = actions,
                selectedTypeId = data.product.typeId,
                typeError = data.typeError
            )
            UnitColumn(
                modifier = Modifier.fillMaxWidth(1f),
                units = data.units,
                actions = actions,
                selectedUnitId = data.product.unitId,
                unitError = data.unitError
            )
        }



        Spacer(modifier = Modifier.weight(1.0f))
        AddEditRow(
            name = data.product.name,
            id = data.product.id,
            onAddEdit = { actions.addEditProduct() },
            onDelete = {
                actions.deleteProduct()
            }
        )
    }
}

@Composable
fun ChipRow(
    actions: AddEditProductActions,
    data: AddEditProductData

) {
    Row {
        if (data.chips.isEmpty()) {
            //create first zalozku
        } else {
            LazyRow {
                data.chips.forEach { chip ->
                    item {
                        val selected = data.productChips.find { it == chip.id } != null
                        FilterItem(
                            name = chip.name,
                            id = chip.id!!,
                            actions = actions,
                            selected = selected
                        )
                    }
                }
            }
        }
        CreateChipDialog(actions = actions, data = data)
    }
}


@Composable
fun CreateChipDialog(
    actions: AddEditProductActions,
    data: AddEditProductData
) {
    var isShown by remember {
        mutableStateOf(false)
    }
    Button(onClick = { isShown = !isShown }, shape = ButtonDefaults.outlinedShape) {
        Text(text = "+")
    }


    if (isShown) {
        Dialog(onDismissRequest = { isShown = false }) {
            Column {
                TextFieldWError(
                    label = stringResource(R.string.chip_name),
                    value = data.chip.name,
                    onValueChanged = {
                        actions.chipChanged(it)
                    },
                    error = data.chipError
                )
                Row {
                    Button(onClick = { isShown = false }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Button(onClick = {
                        run {
                            if (actions.addChip()) {
                                isShown = false
                            }
                        }
                    }) {
                        Text(text = stringResource(R.string.add))
                    }
                }
            }

        }
    }
}

@Composable
fun FilterItem(
    name: String,
    id: Long,
    actions: AddEditProductActions,
    selected: Boolean
) {

    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        onClick = {
            actions.chipsChanged(id)
        },
        label = {
            Text(name)
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Composable
fun TypeColumn(
    types: List<Type>,
    actions: AddEditProductActions,
    selectedTypeId: Long,
    modifier: Modifier = Modifier,
    typeError: String?,
) {
    LazyColumn(modifier = modifier) {
        item {
            if (typeError != null) {
                Text(text = typeError!!, color = Color.Red)
            }
        }
        if (types.isEmpty()) {
            item {
                Text("Types are empty")
            }
        } else {
            types.forEach { option ->
                item {
                    Row(modifier = Modifier
                        .clickable {
                            actions.typeChanged(option.id!!)
                        }
                        .background(
                            color =
                            if (option.id == selectedTypeId) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.background
                            }
                        )
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center) {
                        GetIconByName(
                            name = option.iconRoute,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                        Text(option.name)
                    }
                }
            }
        }
    }
}


@Composable
fun UnitColumn(
    units: List<WarehouseUnit>,
    actions: AddEditProductActions,
    selectedUnitId: Long,
    modifier: Modifier = Modifier,
    unitError: String?
) {
    LazyColumn(modifier = modifier) {
        item {
            if (unitError != null) {
                Text(text = unitError!!, color = Color.Red)
            }
        }
        if (units.isEmpty()) {
            item {
                Text("No results found")
            }
        } else {
            units.forEach { option ->
                item {
                    Row(
                        modifier = Modifier
                            .clickable {
                                actions.unitChanged(option.id!!)
                            }
                            .background(
                                color =
                                if (option.id == selectedUnitId) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.background
                                }
                            )
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center)
                    {
                        Text(
                            text = option.shortcut,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                        Text(option.name)
                    }
                }
            }
        }
    }
}
