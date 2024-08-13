package eu.glitch.mywarehouse.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun navigateToAddEditProductScreen(id: String?)
    fun navigateToProductListScreen()

    fun navigateToBarcodeScannerScreen()
    fun navigateFromBarcodeScannerScreen(result: String)


    fun navigateToAddEditRecordScreen(id: String?)
    fun navigateToRecordListScreen()

    fun navigateToAddEditTypeScreen(id: String?)
    fun navigateToTypeListScreen()

    fun navigateToAddEditUnitScreen(id: String?)
    fun navigateToUnitListScreen()

    fun navigateToAddEditChipScreen(id: String?)

    fun navigateToSettingsScreen()

    fun navigateToDestination(destination: Destination)
    fun getCurrentRoute(): String?

    fun getNavController(): NavController
    fun returnBack()
}