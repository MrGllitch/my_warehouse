package eu.glitch.mywarehouse.navigation

import androidx.navigation.NavController
import eu.glitch.mywarehouse.navigation.Destination
import eu.glitch.mywarehouse.navigation.INavigationRouter

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToAddEditProductScreen() {
        navController.navigate(Destination.AddEditProductScreen.route)
    }

    override fun navigateToProductListScreen() {
        navController.navigate(Destination.ProductListScreen.route)
    }

    override fun navigateToAddEditRecordScreen() {
        navController.navigate(Destination.AddEditRecordScreen.route)
    }

    override fun navigateToRecordListScreen() {
        navController.navigate(Destination.RecordListScreen.route)
    }

    override fun navigateToAddEditTypeScreen() {
        navController.navigate(Destination.AddEditTypeScreen.route)
    }

    override fun navigateToTypeListScreen() {
        navController.navigate(Destination.TypeListScreen.route)
    }

    override fun navigateToAddEditUnitScreen() {
        navController.navigate(Destination.AddEditUnitScreen.route)
    }

    override fun navigateToUnitListScreen() {
        navController.navigate(Destination.UnitListScreen.route)
    }

    override fun navigateToDestination(destination: Destination) {
        navController.navigate(destination.route)
    }

    override fun getCurrentRoute(): String? {
        return navController.currentDestination?.route
    }


    override fun getNavController(): NavController {
        return navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }


}