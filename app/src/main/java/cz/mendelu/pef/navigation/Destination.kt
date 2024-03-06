package cz.mendelu.pef.navigation

sealed class Destination(val route: String) {
    object TaskListScreen : Destination("task_list")

    object AddEditTaskScreen : Destination("add_edit_task")

    object MapScreen : Destination("map")
}