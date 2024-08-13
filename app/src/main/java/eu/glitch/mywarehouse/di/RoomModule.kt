package eu.glitch.mywarehouse.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.database.WarehouseDatabase
import eu.glitch.mywarehouse.database.daos.ChipDao
import eu.glitch.mywarehouse.database.daos.ProductDao
import eu.glitch.mywarehouse.database.daos.RecordDao
import eu.glitch.mywarehouse.database.daos.TypeDao
import eu.glitch.mywarehouse.database.daos.UnitDao
import eu.glitch.mywarehouse.database.impl.LocalRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WarehouseDatabase {
        return WarehouseDatabase.getDatabase(context = context)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(repository: LocalRepositoryImpl) : IRepository{
        return repository
    }

    /// Daos

    @Provides
    @Singleton
    fun provideUnitDao(database: WarehouseDatabase): UnitDao {
        return database.UnitDao()
    }
    @Provides
    @Singleton
    fun provideTypeDao(database: WarehouseDatabase): TypeDao {
        return database.TypeDao()
    }
    @Provides
    @Singleton
    fun provideRecordDao(database: WarehouseDatabase): RecordDao {
        return database.RecordDao()
    }
    @Provides
    @Singleton
    fun provideProductDao(database: WarehouseDatabase): ProductDao {
        return database.ProductDao()
    }
    @Provides
    @Singleton
    fun provideChipDao(database: WarehouseDatabase): ChipDao {
        return database.ChipDao()
    }


}