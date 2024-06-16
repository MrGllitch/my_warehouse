package eu.glitch.mywarehouse.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.glitch.mywarehouse.BuildConfig
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.ui.theme.displayFontFamily
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    content: @Composable () -> Unit,
    drawerState: DrawerState, navigationRouter: INavigationRouter
) {

    val versionName = BuildConfig.VERSION_NAME

    val items = listOf(
        NavigationItems(
            title = stringResource(R.string.records),
            selectedIcon = ImageVector.vectorResource(R.drawable.icon_receipt_long),
            unselectedIcon = ImageVector.vectorResource(R.drawable.icon_receipt_long_outline),
            destination = Destination.RecordListScreen
        ),
        NavigationItems(
            title = stringResource(R.string.Products),
            selectedIcon = ImageVector.vectorResource(R.drawable.icon_apps), //apps
            unselectedIcon = ImageVector.vectorResource(R.drawable.icon_apps),
            destination = Destination.ProductListScreen
        ),
        NavigationItems(
            title = stringResource(R.string.types),
            selectedIcon = ImageVector.vectorResource(R.drawable.icon_discount), //discount
            unselectedIcon = ImageVector.vectorResource(R.drawable.icon_discount),
            destination = Destination.TypeListScreen
        ),
        NavigationItems(
            title = stringResource(R.string.units),
            selectedIcon = ImageVector.vectorResource(R.drawable.icon_category), //category
            unselectedIcon = ImageVector.vectorResource(R.drawable.icon_category),
            destination = Destination.UnitListScreen
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier.padding(16.dp),
                fontFamily = displayFontFamily
            )
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
                            } else {
                                item.unselectedIcon
                            }, contentDescription = item.title
                        )
                    },
                    badge = {
                        item.badgeCount?.let {
                            Text(text = item.badgeCount.toString())
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
            Spacer(modifier = Modifier.weight(1.0f))
            HorizontalDivider()
            Row {
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = stringResource(R.string.version) + " " + versionName,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }) {
        content()
    }

}