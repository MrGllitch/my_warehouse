package eu.glitch.mywarehouse.ui.screens.product

import android.net.Uri
import androidx.camera.core.ImageCapture

interface AddEditProductActions {
    fun nameChanged(text: String)
    fun descriptionChanged(text: String)
    fun typeChanged(id: Long)
    fun photoChanged(uri: Uri)
    fun barcodeChanged(barcode: String)
    fun takePhoto(imageCapture: ImageCapture)

    fun addEditProduct()
    fun deleteProduct()
}