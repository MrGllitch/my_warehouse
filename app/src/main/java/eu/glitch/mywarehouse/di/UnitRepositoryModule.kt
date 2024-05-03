package eu.glitch.mywarehouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import eu.glitch.mywarehouse.database.product.LocalProductRepositoryImpl
import eu.glitch.mywarehouse.database.product.ProductDao
import eu.glitch.mywarehouse.database.unit.ILocalUnitRepository
import eu.glitch.mywarehouse.database.unit.LocalUnitRepositoryImpl
import eu.glitch.mywarehouse.database.unit.UnitDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UnitRepositoryModule {
    @Provides
    @Singleton
    fun provideUnitRepository(unitDao: UnitDao): ILocalUnitRepository {
        return LocalUnitRepositoryImpl(unitDao)
    }
}
