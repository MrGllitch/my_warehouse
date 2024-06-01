package eu.glitch.mywarehouse.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToAddEditProductScreen(id: Long?) {
        if (id == null) {
            navController.navigate(Destination.AddEditProductScreen.route)
        } else {
            navController.navigate(Destination.AddEditProductScreen.route + "/" + id)
        }


    }

    override fun navigateToProductListScreen() {
        navController.navigate(Destination.ProductListScreen.route)
    }

    override fun navigateToBarcodeScannerScreen() {
        navController.navigate(Destination.BarcodeScannerScreen.route)
    }

    override fun navigateFromBarcodeScannerScreen(result: String) {
        navController.currentBackStackEntry?.savedStateHandle?.set("result", result)
        returnBack()
    }


    override fun navigateToAddEditRecordScreen(id: Long?) {
        if (id == null) {
            navController.navigate(Destination.AddEditRecordScreen.route)
        } else {
            navController.navigate(Destination.AddEditRecordScreen.route + "/" + id)
        }

    }

    override fun navigateToRecordListScreen() {
        navController.navigate(Destination.RecordListScreen.route)
    }

    override fun navigateToAddEditTypeScreen(id: Long?) {
        if (id == null) {
            navController.navigate(Destination.AddEditTypeScreen.route)
        } else {
            navController.navigate(Destination.AddEditTypeScreen.route + "/" + id)
        }

    }

    override fun navigateToTypeListScreen() {
        navController.navigate(Destination.TypeListScreen.route)
    }

    override fun navigateToAddEditUnitScreen(id: Long?) {
        if (id == null) {
            navController.navigate(Destination.AddEditUnitScreen.route)
        } else {
            navController.navigate(Destination.AddEditUnitScreen.route + "/" + id)
        }

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
