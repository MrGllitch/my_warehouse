package eu.glitch.mywarehouse.ui.screens.product

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import eu.glitch.mywarehouse.database.type.ILocalTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    private val productRepository: ILocalProductRepository,
    private val typeRepository: ILocalTypeRepository,
    @ApplicationContext val context: Context
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


    override fun typeChanged(id: Long) {
        data.product.typeId = id
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
        if (data.product.typeId == null || data.product.typeId <= 0) {
            data.typeError = "You have to select type"
            _addEditProductUIState.update {
                AddEditProductUIState.ProductStateChanged(data)
            }
            return
        } else {
            data.typeError = null
        }
        data.product.imageName = data.imageName
        if (data.product.id == null) {
            viewModelScope.launch {
                productRepository.insertProduct(data.product)
            }
        } else {
            viewModelScope.launch {
                productRepository.updateProduct(data.product)
            }
        }

        _addEditProductUIState.update {
            AddEditProductUIState.Success
        }
    }

    override fun deleteProduct() {
        removePhoto()
        viewModelScope.launch {
            productRepository.deleteProduct(data.product)
        }
        _addEditProductUIState.update {
            AddEditProductUIState.Success
        }
    }

    fun loadProduct(id: Long) {

        viewModelScope.launch {
            data.product = productRepository.getProduct(id)
        }
        data.imageName = data.product.imageName.toString()
        _addEditProductUIState.update {
            AddEditProductUIState.ProductStateChanged(data)
        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            typeRepository.getTypes().collect() {
                data.types = it.toMutableList()
                _addEditProductUIState.update {
                    AddEditProductUIState.ProductStateChanged(data)
                }
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
            "yyyy-MM-dd-HH-mm-ss-SSS",
            Locale.GERMANY
        ).format(System.currentTimeMillis()) + ".jpg"

        val photoFile = File(
            context.filesDir,
            imageName
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
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

}