package eu.glitch.mywarehouse.ui.screens.product

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Type

class AddEditProductData {
    var product: Product = Product(null, "", null)
    var types: MutableList<Type> = mutableListOf()
    var nameError: String? = null
    var typeError: String? = null
    var imageError: String? = null
}