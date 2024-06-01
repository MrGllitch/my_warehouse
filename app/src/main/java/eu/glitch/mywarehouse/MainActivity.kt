package eu.glitch.mywarehouse

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint
import eu.glitch.mywarehouse.navigation.Destination
import eu.glitch.mywarehouse.navigation.NavGraph
import eu.glitch.mywarehouse.ui.theme.MyWarehouseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            PackageManager.PERMISSION_GRANTED
        }

        super.onCreate(savedInstanceState)
        setContent {
            MyWarehouseTheme {
                NavGraph(startDestination = Destination.ProductListScreen.route)

            }
        }
    }
}
