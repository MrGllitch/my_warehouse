package eu.glitch.mywarehouse.navigation

sealed class Destination(val route: String) {
    object AddEditProductScreen : Destination("add_edit_product")
    object ProductListScreen : Destination("product_list")

    object AddEditRecordScreen : Destination("add_edit_record")
    object RecordListScreen : Destination("record_list")

    object AddEditTypeScreen : Destination("add_edit_type")
    object TypeListScreen : Destination("type_list")

    object AddEditUnitScreen : Destination("add_edit_unit")
    object UnitListScreen : Destination("unit_list_screen")

    object BarcodeScannerScreen : Destination("barcode_scanner")
    object SettingsScreen : Destination("settings")

    object AddEditChipScreen : Destination("add_edit_chip")

}