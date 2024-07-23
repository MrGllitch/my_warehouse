package eu.glitch.mywarehouse.ui.screens.product

import android.net.Uri
import androidx.camera.core.ImageCapture

interface AddEditProductActions {
    fun nameChanged(text: String)
    fun typeChanged(id: Long)
    fun unitChanged(id: Long)
    fun photoChanged(uri: Uri)
    fun barcodeChanged(barcode: String)
    fun takePhoto(imageCapture: ImageCapture)
    fun removePhoto()
    fun chipsChanged(id: Long)
    fun chipChanged(text: String)


    fun addChip(): Boolean
    fun addEditProduct()
    fun deleteProduct()
}