package eu.glitch.mywarehouse.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.intPreferencesKey
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.dataStore
import eu.glitch.mywarehouse.database.type.ILocalTypeRepository
import eu.glitch.mywarehouse.database.unit.ILocalUnitRepository
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object SeedDatabase {

    suspend fun seed(
        typeRepository: ILocalTypeRepository,
        unitRepository: ILocalUnitRepository,
        context: Context,
    ) {
        Log.d("seed", "seed: start")
        val launchCount =
            context.dataStore.data.map { it[intPreferencesKey("isFirstRun")] ?: 0 }.first()
        var units = unitRepository.getUnitsSize()
        Log.d("seed", "seed: $launchCount, $units")
        if (launchCount == 1 && units == 0) {
            typeRepository.insertType(
                Type(null, context.getString(R.string.kolonial), "icon_light_mode")
            )
            typeRepository.insertType(
                Type(null, context.getString(R.string.chlazene), "icon_thermostat")
            )
            typeRepository.insertType(
                Type(null, context.getString(R.string.mrazene), "icon_ac_unit")
            )
            unitRepository.insertUnit(
                WarehouseUnit(null, context.getString(R.string.gram), "G")
            )
            unitRepository.insertUnit(
                WarehouseUnit(null, context.getString(R.string.kilogram), "KG")
            )
            unitRepository.insertUnit(
                WarehouseUnit(null, context.getString(R.string.mililitr), "ML")
            )
            unitRepository.insertUnit(
                WarehouseUnit(null, context.getString(R.string.litr), "L")
            )
            unitRepository.insertUnit(
                WarehouseUnit(null, context.getString(R.string.kus), context.getString(R.string.ks))
            )
        }
    }
}