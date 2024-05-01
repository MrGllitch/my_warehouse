package eu.glitch.mywarehouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.database.RepositoryImpl
import eu.glitch.mywarehouse.database.ProductDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTasksRepository(dao: ProductDao): IRepository {
        return RepositoryImpl(dao)
    }
}
