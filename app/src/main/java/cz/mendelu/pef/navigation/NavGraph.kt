package cz.mendelu.pef.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.ui.screens.AddEditTaskScreen
import cz.mendelu.pef.ui.screens.MapScreen
import cz.mendelu.pef.ui.screens.TaskListScreen

@Composable
fun NavGraph(
    navHostController: NavHostController = rememberNavController(),
    navigationRouter: INavigationRouter = remember {
        NavigationRouterImpl(navController = navHostController)
    },
    startDestination: String
){
    NavHost(navController = navHostController, startDestination = startDestination){

        composable(Destination.TaskListScreen.route){
            TaskListScreen(navigationRouter)
        }
        composable(Destination.AddEditTaskScreen.route){
            AddEditTaskScreen(navigationRouter)
        }
        composable(Destination.MapScreen.route){
            MapScreen()
        }

    }

}

