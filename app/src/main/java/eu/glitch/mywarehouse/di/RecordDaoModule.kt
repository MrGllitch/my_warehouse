package eu.glitch.mywarehouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.WarehouseDatabase
import eu.glitch.mywarehouse.database.record.RecordDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecordDaoModule {
    @Provides
    @Singleton
    fun provideDao(database: WarehouseDatabase): RecordDao {
        return database.RecordDao()
    }
}
