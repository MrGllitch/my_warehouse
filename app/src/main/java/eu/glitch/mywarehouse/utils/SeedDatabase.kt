package eu.glitch.mywarehouse.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.intPreferencesKey
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.dataStore
import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.database.impl.RepositoryImpl
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object SeedDatabase {

    suspend fun seed(
        repository: RepositoryImpl,
        context: Context,
    ) {
        Log.d("seed", "seed: start")
        val launchCount =
            context.dataStore.data.map { it[intPreferencesKey("isFirstRun")] ?: 0 }.first()
        var units = repository.getUnitsSize()
        Log.d("seed", "seed: $launchCount, $units")
        if (launchCount == 1 && units == 0) {
            repository.insertType(
                Type("", context.getString(R.string.kolonial), "icon_light_mode")
            )
            repository.insertType(
                Type("", context.getString(R.string.chlazene), "icon_thermostat")
            )
            repository.insertType(
                Type("", context.getString(R.string.mrazene), "icon_ac_unit")
            )
            repository.insertUnit(
                WarehouseUnit("", context.getString(R.string.gram), "G")
            )
            repository.insertUnit(
                WarehouseUnit("", context.getString(R.string.kilogram), "KG")
            )
            repository.insertUnit(
                WarehouseUnit("", context.getString(R.string.mililitr), "ML")
            )
            repository.insertUnit(
                WarehouseUnit("", context.getString(R.string.litr), "L")
            )
            repository.insertUnit(
                WarehouseUnit("", context.getString(R.string.kus), context.getString(R.string.ks))
            )
        }
    }
}