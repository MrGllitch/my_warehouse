package eu.glitch.mywarehouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import eu.glitch.mywarehouse.database.product.LocalProductRepositoryImpl
import eu.glitch.mywarehouse.database.product.ProductDao
import eu.glitch.mywarehouse.database.type.ILocalTypeRepository
import eu.glitch.mywarehouse.database.type.LocalTypeRepositoryImpl
import eu.glitch.mywarehouse.database.type.TypeDao

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TypeRepositoryModule {
    @Provides
    @Singleton
    fun provideTypeRepository(typeDao: TypeDao): ILocalTypeRepository {
        return LocalTypeRepositoryImpl(typeDao)
    }
}
