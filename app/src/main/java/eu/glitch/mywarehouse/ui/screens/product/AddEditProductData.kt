package eu.glitch.mywarehouse.ui.screens.product

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Type

class AddEditProductData {
    var product: Product = Product(null, "", 0, "", "")
    var types: MutableList<Type> = mutableListOf()
    var nameError: String? = null
    var typeError: String? = null
    var imageName: String = ""
}