package eu.glitch.mywarehouse.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun navigateToAddEditProductScreen(id: Long?)
    fun navigateToProductListScreen()

    fun navigateToAddEditRecordScreen(id: Long?)
    fun navigateToRecordListScreen()

    fun navigateToAddEditTypeScreen(id: Long?)
    fun navigateToTypeListScreen()

    fun navigateToAddEditUnitScreen(id: Long?)
    fun navigateToUnitListScreen()

    fun navigateToDestination(destination: Destination)
    fun getCurrentRoute(): String?

    fun getNavController(): NavController
    fun returnBack()
}