package eu.glitch.mywarehouse.ui.screens.product

import eu.glitch.mywarehouse.model.Chip
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit

class AddEditProductData {
    var product: Product = Product("", "", "", "", "", "")
    var productChips: MutableList<String> = mutableListOf()
    var chip = Chip("", "")

    var types: MutableList<Type> = mutableListOf()
    var units: MutableList<WarehouseUnit> = mutableListOf()
    var chips: MutableList<Chip> = mutableListOf()


    var chipError: String? = null

    var nameError: String? = null
    var typeError: String? = null
    var unitError: String? = null

    var imageName: String = ""
}