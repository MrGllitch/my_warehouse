package eu.glitch.mywarehouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import eu.glitch.mywarehouse.navigation.Destination
import eu.glitch.mywarehouse.navigation.NavGraph
import eu.glitch.mywarehouse.ui.theme.MyWarehouseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MyWarehouseTheme {
                NavGraph(startDestination = Destination.ProductListScreen.route)

            }
        }
    }
}
