package eu.glitch.mywarehouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import dagger.hilt.android.AndroidEntryPoint
import eu.glitch.mywarehouse.ui.theme.MyWarehouseTheme
import eu.glitch.mywarehouse.navigation.Destination
import eu.glitch.mywarehouse.navigation.NavGraph
import eu.glitch.mywarehouse.navigation.NavigationDrawer

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWarehouseTheme {
                NavGraph(startDestination = Destination.ProductListScreen.route)

            }
        }
    }
}
