package eu.glitch.mywarehouse.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun navigateToAddEditProductScreen()
    fun navigateToProductListScreen()

    fun navigateToAddEditRecordScreen()
    fun navigateToRecordListScreen()

    fun navigateToAddEditTypeScreen()
    fun navigateToTypeListScreen()

    fun navigateToAddEditUnitScreen()
    fun navigateToUnitListScreen()

    fun navigateToDestination(destination: Destination)
    fun getCurrentRoute() : String?

    fun getNavController(): NavController
    fun returnBack()
}