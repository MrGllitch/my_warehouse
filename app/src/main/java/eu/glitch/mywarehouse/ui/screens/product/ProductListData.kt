package eu.glitch.mywarehouse.ui.screens.product

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Type

class ProductListData {
    var products: MutableList<Product> = mutableListOf()
    var types: MutableList<Type> = mutableListOf()
}