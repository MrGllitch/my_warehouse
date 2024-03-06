package cz.mendelu.pef.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter{
    override fun navigateToAddEditTask(id: Long?) {
        navController.navigate(Destination.AddEditTaskScreen.route)
    }
    override fun navigateToMap() {
        navController.navigate(Destination.MapScreen.route)
    }
    override fun getNavController(): NavController {
        return navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }
}