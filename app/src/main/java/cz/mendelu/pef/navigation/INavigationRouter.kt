package cz.mendelu.pef.navigation

import androidx.navigation.NavController

interface INavigationRouter {

    fun navigateToAddEditTask(id: Long?)
    fun navigateToMap()
    fun getNavController(): NavController
    fun returnBack()
}