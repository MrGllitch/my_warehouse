package eu.glitch.mywarehouse.ui.screens.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.ui.elements.AddEditRow
import eu.glitch.mywarehouse.ui.elements.CameraView
import eu.glitch.mywarehouse.ui.elements.GetIconByName
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
    val context = LocalContext.current

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
        var isCameraVisible by remember {
            mutableStateOf(false)
        }
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
            Column {
                Button(onClick = { isCameraVisible = true }) {
                    if (data.imageName == "") {
                        Text(text = "Take photo")
                    } else {
                        Text(text = "Retake photo")
                    }
                }
                if (isCameraVisible) {
                    CameraView(
                        takePhoto = { actions.takePhoto(it) },
                        onDismissRequest = { isCameraVisible = false })
                }
                BarcodeSelector(data = data, navigationRouter = navigationRouter, actions = actions)
            }

        }


        TypeSelector(data = data, actions = actions)

        Spacer(modifier = Modifier.weight(1.0f))
        AddEditRow(
            name = data.product.name,
            id = data.product.id,
            onAddEdit = { actions.addEditProduct() },
            onCancel = { navigationRouter.returnBack() }) {

        }
    }
}

@Composable
fun BarcodeSelector(
    data: AddEditProductData,
    navigationRouter: INavigationRouter,
    actions: AddEditProductActions
) {
    Column {
        Row(modifier = Modifier.clickable { navigationRouter.navigateToBarcodeScannerScreen() }) {
            GetIconByName(name = "icon_barcode", contentDescription = "")
            if (data.barcode == "") {
                Text(text = "Scan code")
            } else {
                Text(text = "Rescan code")
                IconButton(onClick = { actions.barcodeChanged("") }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
            }
            Text(text = "${data.barcode}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeSelector(
    data: AddEditProductData,
    actions: AddEditProductActions
) {

    var isItemTypeOpen by remember { mutableStateOf(false) }
    var selectedItem =
        data.types.find { type -> type.id == data.product.typeId } ?: Type(null, "", "")
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

}

