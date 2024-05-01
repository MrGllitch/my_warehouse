package eu.glitch.mywarehouse.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.glitch.mywarehouse.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = true)
abstract class WarehouseDatabase : RoomDatabase() {

    abstract fun namesDao(): ProductDao

    companion object {
        private var INSTANCE: WarehouseDatabase? = null
        fun getDatabase(context: Context): WarehouseDatabase {
            if (INSTANCE == null) {
                synchronized(WarehouseDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            WarehouseDatabase::class.java, "tasks_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
