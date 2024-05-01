package eu.glitch.mywarehouse.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
){
    NavHost(navController = navHostController, startDestination = startDestination){
        composable(Destination.AddEditProductScreen.route){
            AddEditProductScreen(navigationRouter)
        }

        composable(Destination.ProductListScreen.route){
            ProductListScreen(navigationRouter)
        }

        composable(Destination.AddEditRecordScreen.route){
            AddEditRecordScreen(navigationRouter)
        }

        composable(Destination.RecordListScreen.route){
            RecordListScreen(navigationRouter)
        }

        composable(Destination.AddEditTypeScreen.route){
            AddEditTypeScreen(navigationRouter)
        }

        composable(Destination.TypeListScreen.route){
            TypeListScreen(navigationRouter)
        }

        composable(Destination.AddEditUnitScreen.route){
            AddEditUnitScreen(navigationRouter)
        }

        composable(Destination.UnitListScreen.route){
            UnitListScreen(navigationRouter)
        }
    }

}
