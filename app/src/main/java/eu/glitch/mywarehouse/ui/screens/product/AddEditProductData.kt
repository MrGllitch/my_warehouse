package eu.glitch.mywarehouse.ui.screens.product

import com.ujizin.camposer.state.CameraState
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Type

class AddEditProductData {
    var product: Product = Product(null, "", 0, "", "")
    var types: MutableList<Type> = mutableListOf()
    var nameError: String? = null
    var typeError: String? = null
    var imageName: String = ""
    var barcode: String = ""
    var cameraState: CameraState? = null
}