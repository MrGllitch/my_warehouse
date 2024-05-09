package eu.glitch.mywarehouse.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(content: @Composable () -> Unit,
                     drawerState: DrawerState, navigationRouter: INavigationRouter)  {

    val items = listOf(
        NavigationItems(
            title = "Zaznamy",
            selectedIcon = Icons.Filled.Delete,
            unselectedIcon = Icons.Outlined.Delete,
            destination = Destination.RecordListScreen
        ),
        NavigationItems(
            title = "Produkty",
            selectedIcon = Icons.Filled.AccountBox,
            unselectedIcon = Icons.Outlined.AccountBox,
            destination = Destination.ProductListScreen
        ),
        NavigationItems(
            title = "Typy",
            selectedIcon = Icons.Filled.Face,
            unselectedIcon = Icons.Outlined.Face,
            destination = Destination.TypeListScreen
        ),
        NavigationItems(
            title = "Jednotky",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            destination = Destination.UnitListScreen
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Text(text = "Muj sklad", modifier = Modifier.padding(16.dp))
            HorizontalDivider()
            items.forEachIndexed { index, item ->
                androidx.compose.material3.NavigationDrawerItem(
                    label = { Text(text = item.title) },
                    selected = navigationRouter.getCurrentRoute() == item.destination.route,
                    onClick = {
                        navigationRouter.navigateToDestination(item.destination)

                        selectedItemIndex = index
                        scope.launch {
                            drawerState.close()
                        } 
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                                                          item.selectedIcon
                            }else{
                                 item.unselectedIcon
                                 }, contentDescription = item.title
                        )
                    },
                    badge = {
                        item.badgeCount?.let{
                            Text(text = item.badgeCount.toString())
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }) {
            content()
    }

}