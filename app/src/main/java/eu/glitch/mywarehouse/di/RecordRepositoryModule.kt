package eu.glitch.mywarehouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import eu.glitch.mywarehouse.database.product.LocalProductRepositoryImpl
import eu.glitch.mywarehouse.database.product.ProductDao
import eu.glitch.mywarehouse.database.record.ILocalRecordRepository
import eu.glitch.mywarehouse.database.record.LocalRecordRepositoryImpl
import eu.glitch.mywarehouse.database.record.RecordDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecordRepositoryModule {
    @Provides
    @Singleton
    fun provideRecordRepository(recordDao: RecordDao): ILocalRecordRepository {
        return LocalRecordRepositoryImpl(recordDao)
    }
}
