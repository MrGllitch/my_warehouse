package eu.glitch.mywarehouse.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.glitch.mywarehouse.ui.screens.BarcodeScannerScreen
import eu.glitch.mywarehouse.ui.screens.product.AddEditProductScreen
import eu.glitch.mywarehouse.ui.screens.product.ProductListScreen
import eu.glitch.mywarehouse.ui.screens.record.AddEditRecordScreen
import eu.glitch.mywarehouse.ui.screens.record.RecordListScreen
import eu.glitch.mywarehouse.ui.screens.type.AddEditTypeScreen
import eu.glitch.mywarehouse.ui.screens.type.TypeListScreen
import eu.glitch.mywarehouse.ui.screens.unit.AddEditUnitScreen
import eu.glitch.mywarehouse.ui.screens.unit.UnitListScreen

@Composable
fun NavGraph(
    navHostController: NavHostController = rememberNavController(),
    navigationRouter: INavigationRouter = remember {
        NavigationRouterImpl(navController = navHostController)
    },
    startDestination: String
) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(Destination.AddEditProductScreen.route) {
            AddEditProductScreen(navigationRouter, null)
        }
        composable(Destination.AddEditProductScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )) {
            val id = it.arguments?.getLong("id")
            AddEditProductScreen(
                navigationRouter = navigationRouter,
                id = id
            )
        }

        composable(Destination.ProductListScreen.route) {
            ProductListScreen(navigationRouter)
        }

        composable(Destination.AddEditRecordScreen.route) {
            AddEditRecordScreen(navigationRouter, id = null)
        }

        composable(Destination.AddEditRecordScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )) {
            val id = it.arguments?.getLong("id")
            AddEditRecordScreen(
                navigationRouter = navigationRouter,
                id = id
            )
        }

        composable(Destination.RecordListScreen.route) {
            RecordListScreen(navigationRouter)
        }

        composable(Destination.AddEditTypeScreen.route) {
            AddEditTypeScreen(navigationRouter, null)
        }
        composable(Destination.AddEditTypeScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )) {
            val id = it.arguments?.getLong("id")
            AddEditTypeScreen(
                navigationRouter = navigationRouter,
                id = id
            )
        }

        composable(Destination.TypeListScreen.route) {
            TypeListScreen(navigationRouter)
        }

        composable(Destination.AddEditUnitScreen.route) {
            AddEditUnitScreen(navigationRouter, null)
        }
        composable(Destination.AddEditUnitScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )) {
            val id = it.arguments?.getLong("id")
            AddEditUnitScreen(
                navigationRouter = navigationRouter,
                id = id
            )
        }


        composable(Destination.UnitListScreen.route) {
            UnitListScreen(navigationRouter)
        }
        composable(Destination.BarcodeScannerScreen.route) {
            BarcodeScannerScreen(navigationRouter = navigationRouter)
        }

    }

}
