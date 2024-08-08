package eu.glitch.mywarehouse

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import eu.glitch.mywarehouse.navigation.Destination
import eu.glitch.mywarehouse.navigation.NavGraph
import eu.glitch.mywarehouse.ui.theme.MyWarehouseTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_warehouse")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val FIRST_RUN_COUNTER = intPreferencesKey("isFirstRun")
    var run_count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        lifecycleScope.launch {
            writeRun()
            run_count = readRun()
            setContent {
                MyWarehouseTheme {
                    if (run_count > 1) {
                        NavGraph(startDestination = Destination.RecordListScreen.route)
                    } else {
                        NavGraph(startDestination = Destination.ProductListScreen.route)
                    }
                }
            }
        }

    }

    private suspend fun writeRun() {
        dataStore.edit {
            val currentCounterValue = it[FIRST_RUN_COUNTER] ?: 0
            it[FIRST_RUN_COUNTER] = currentCounterValue + 1
        }
    }

    private suspend fun readRun(): Int {
        val launchCount = dataStore.data.map { it[FIRST_RUN_COUNTER] ?: 0 }.first()
        return launchCount
    }
}
