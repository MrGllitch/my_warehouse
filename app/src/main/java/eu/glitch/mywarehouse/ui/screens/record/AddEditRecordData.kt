package eu.glitch.mywarehouse.ui.screens.record

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.WarehouseUnit

class AddEditRecordData {
    var record = Record(null, 0, 0, 0, 0, 0)

    var products: MutableList<Product> = mutableListOf()

    var units: MutableList<WarehouseUnit> = mutableListOf()

    var quantityError: String? = null
    var productError: String? = null
    var dateError: String? = null
}