package eu.glitch.mywarehouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import eu.glitch.mywarehouse.database.product.LocalProductRepositoryImpl
import eu.glitch.mywarehouse.database.product.ProductDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductRepositoryModule {
    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao): ILocalProductRepository {
        return LocalProductRepositoryImpl(productDao)
    }
}
