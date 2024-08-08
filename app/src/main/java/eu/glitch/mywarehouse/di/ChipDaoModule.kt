package eu.glitch.mywarehouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.WarehouseDatabase
import eu.glitch.mywarehouse.database.chip.ChipDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChipDaoModule {
    @Provides
    @Singleton
    fun provideDao(database: WarehouseDatabase): ChipDao {
        return database.ChipDao()
    }
}
