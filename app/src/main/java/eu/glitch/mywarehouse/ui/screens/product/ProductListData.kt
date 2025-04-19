package eu.glitch.mywarehouse.ui.screens.product

import eu.glitch.mywarehouse.model.Chip
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Type

class ProductListData {
    var products: MutableList<Product> = mutableListOf()
    var filteredProducts: MutableList<Product> = mutableListOf()
    var types: MutableList<Type> = mutableListOf()
    var chips: MutableList<Chip> = mutableListOf()
    var isAdmin: Boolean = true

    var filters: MutableMap<String, MutableList<String>> = mutableMapOf()
}