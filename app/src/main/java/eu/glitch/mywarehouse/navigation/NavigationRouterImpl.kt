package eu.glitch.mywarehouse.navigation

import androidx.navigation.NavController
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import eu.glitch.mywarehouse.model.Barcode

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToAddEditProductScreen(id: String?) {
        decideRoute(Destination.AddEditProductScreen, id)
    }

    override fun navigateToProductListScreen() {
        navController.navigate(Destination.ProductListScreen.route)
    }

    override fun navigateToBarcodeScannerScreen() {
        navController.navigate(Destination.BarcodeScannerScreen.route)
    }

    override fun navigateFromBarcodeScannerScreen(result: String) {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Barcode> = moshi.adapter(Barcode::class.java)
        navController.previousBackStackEntry?.savedStateHandle?.set(
            "barcode",
            jsonAdapter.toJson(Barcode(result))
        )
        returnBack()
    }


    override fun navigateToAddEditRecordScreen(id: String?) {
        decideRoute(Destination.AddEditRecordScreen, id)

    }

    override fun navigateToRecordListScreen() {
        navController.navigate(Destination.RecordListScreen.route)
    }

    override fun navigateToAddEditTypeScreen(id: String?) {
        decideRoute(Destination.AddEditTypeScreen, id)

    }

    override fun navigateToTypeListScreen() {
        navController.navigate(Destination.TypeListScreen.route)
    }

    override fun navigateToAddEditUnitScreen(id: String?) {
        decideRoute(Destination.AddEditUnitScreen, id)

    }

    override fun navigateToUnitListScreen() {
        navController.navigate(Destination.UnitListScreen.route)
    }

    override fun navigateToAddEditChipScreen(id: String?) {
        decideRoute(Destination.AddEditChipScreen, id)
    }

    override fun navigateToSettingsScreen() {
        navController.navigate(Destination.SettingsScreen.route)
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

    private fun decideRoute(destination: Destination, id: String?){
        if (id == "" || id == null) {
            navController.navigate(destination.route)
        } else {
            navController.navigate(destination.route + "/" + id!!)
        }
    }


}
