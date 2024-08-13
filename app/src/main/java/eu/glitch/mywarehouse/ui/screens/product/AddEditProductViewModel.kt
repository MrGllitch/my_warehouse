package eu.glitch.mywarehouse.ui.screens.product

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.glitch.mywarehouse.database.impl.RepositoryImpl
import eu.glitch.mywarehouse.model.ProductChipCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    private val repository: RepositoryImpl, @ApplicationContext val context: Context
) : ViewModel(), AddEditProductActions {


    var data: AddEditProductData = AddEditProductData()

    private val _addEditProductUIState: MutableStateFlow<AddEditProductUIState> =
        MutableStateFlow(value = AddEditProductUIState.Loading)
    val addEditProductUIState = _addEditProductUIState.asStateFlow()
    override fun nameChanged(text: String) {
        if (text.length < 64) {
            data.product.name = text
        }
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }


    override fun typeChanged(id: String) {
        data.product.typeId = id
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    override fun unitChanged(id: String) {
        data.product.unitId = id
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    override fun photoChanged(uri: Uri) {
    }

    override fun barcodeChanged(barcode: String) {
        data.product.barcode = barcode
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    override fun addEditProduct() {
        if (data.product.name.isEmpty()) {
            data.nameError = "Name cannot be empty"
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
            return
        } else {
            data.nameError = null
        }
        if (data.product.typeId == "") {
            data.typeError = "You have to select type"
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
            return
        } else {
            data.typeError = null
        }
        if (data.product.unitId == "") {
            data.unitError = "You have to select unit"
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
            return
        } else {
            data.typeError = null
        }
        data.product.imageName = data.imageName
        Log.e("insert product", "insert: calling ${data.product.id}")
        viewModelScope.launch {
            if (data.product.id == "") {
                val id = repository.insertProduct(data.product)
                withContext(Dispatchers.IO) {
                    data.productChips.forEach {
                        repository.insertProductChipCrossRef(
                            ProductChipCrossRef(
                                productId = id, chipId = it
                            )
                        )
                    }
                }
            } else {
                repository.updateProductWithChips(data.product, data.productChips)
            }
            _addEditProductUIState.update {
                AddEditProductUIState.Success
            }
        }
    }

    override fun deleteProduct() {
        removePhoto()
        viewModelScope.launch {
            repository.deleteProduct(data.product)
            _addEditProductUIState.update {
                AddEditProductUIState.Success
            }
        }

    }

    fun loadProduct(id: String) {
        viewModelScope.launch {
            data.product = repository.getProduct(id)

            data.productChips = repository.getChipIdsForProduct(data.product.id).toMutableList()
        }
        data.imageName = data.product.imageName.toString()
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            data.types = repository.getTypes().toMutableList()
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
        }
    }

    fun loadUnits() {
        viewModelScope.launch {
            data.units = repository.getUnits().toMutableList()
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }

        }
    }

    fun loadChips() {
        viewModelScope.launch {
            data.chips = repository.getChips().toMutableList()
            Log.d("load", "loadChips: ${data.chips}")
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
        }
    }

    override fun takePhoto(
        imageCapture: ImageCapture,
    ) {
        if (data.imageName != "") {
            removePhoto()
        }

        var imageName = SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS", Locale.GERMANY
        ).format(System.currentTimeMillis()) + ".jpg"

        val photoFile = File(
            context.filesDir, imageName
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions,
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    data.imageName = imageName
                    _addEditProductUIState.update {
                        AddEditProductUIState.ProductStateChanged(data)
                    }
                }
            })
    }

    override fun removePhoto() {
        context.deleteFile(data.imageName)
        data.imageName = ""

        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }


    override fun chipsChanged(id: String) {
        val chipId = data.productChips.find { it == id }
        if (chipId != null) {
            data.productChips.remove(chipId)
        } else {
            data.productChips.add(id)
        }
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    override fun chipChanged(text: String) {
        data.chip.name = text
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    override fun addChip(): Boolean {
        if (data.chip.name.isEmpty()) {
            data.chipError = "Name cannot be empty"
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
            return false
        } else {
            data.chipError = null
        }
        if (data.chip.name.length <= 2) {
            data.chipError = "Name has to be at least 3 letters long"
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
            return false
        } else {
            data.chipError = null
        }



        viewModelScope.launch {
            val chipId = repository.insertChip(data.chip)
            chipsChanged(chipId)
            data.chip.name = ""
            loadChips()

        }
        return true
    }

}