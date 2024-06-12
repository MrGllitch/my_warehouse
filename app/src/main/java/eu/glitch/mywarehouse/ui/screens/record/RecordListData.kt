package eu.glitch.mywarehouse.ui.screens.record

import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit

class RecordListData {
    var records: MutableList<Record> = mutableListOf()
    var products: MutableList<Product> = mutableListOf()
    var units: MutableList<WarehouseUnit> = mutableListOf()
    var types: MutableList<Type> = mutableListOf()

    var editRecord: Record? = null
    var tempQuantity: String = ""
    var editName = ""
    var unit = ""
    var editRecordQuantityError: String? = null
}