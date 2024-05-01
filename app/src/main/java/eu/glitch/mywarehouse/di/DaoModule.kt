package eu.glitch.mywarehouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.ProductDao
import eu.glitch.mywarehouse.database.WarehouseDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideDao(database: WarehouseDatabase): ProductDao {
        return database.namesDao()
    }
}
