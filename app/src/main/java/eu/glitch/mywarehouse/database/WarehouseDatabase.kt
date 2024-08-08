package eu.glitch.mywarehouse.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.glitch.mywarehouse.database.chip.ChipDao
import eu.glitch.mywarehouse.database.product.ProductDao
import eu.glitch.mywarehouse.database.record.RecordDao
import eu.glitch.mywarehouse.database.type.TypeDao
import eu.glitch.mywarehouse.database.unit.UnitDao
import eu.glitch.mywarehouse.model.Chip
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.model.ProductChipCrossRef
import eu.glitch.mywarehouse.model.Record
import eu.glitch.mywarehouse.model.Type
import eu.glitch.mywarehouse.model.WarehouseUnit

@Database(
    entities = [Type::class, WarehouseUnit::class, Product::class, Record::class, Chip::class, ProductChipCrossRef::class],
    version = 17,
    exportSchema = true
)
abstract class WarehouseDatabase : RoomDatabase() {

    abstract fun ProductDao(): ProductDao
    abstract fun RecordDao(): RecordDao
    abstract fun TypeDao(): TypeDao
    abstract fun UnitDao(): UnitDao
    abstract fun ChipDao(): ChipDao

    companion object {
        private var INSTANCE: WarehouseDatabase? = null
        fun getDatabase(context: Context): WarehouseDatabase {
            if (INSTANCE == null) {
                synchronized(WarehouseDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            WarehouseDatabase::class.java, "tasks_database"
                        ) //.fallbackToDestructiveMigration()
                    .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
