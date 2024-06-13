package eu.glitch.mywarehouse.ui.screens.record

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.WarehouseUnit

class AddEditRecordData {
    var record = ScreenRecord(null, "", "", "", "0", null, "")

    var products: MutableList<Product> = mutableListOf()

    var units: MutableList<WarehouseUnit> = mutableListOf()

    var quantityError: String? = null
    var productError: String? = null
    var unitError: String? = null
    var quantity: String? = null
    var dateError: String? = null
}

data class ScreenRecord(
    var id: Long?,
    var productId: String,
    var unitId: String,
    var typeId: String,
    var quantity: String,
    var dateFrom: String?,
    var dateTo: String
)