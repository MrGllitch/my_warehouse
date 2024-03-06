package cz.mendelu.pef.di

import cz.mendelu.pef.database.ILocalTasksRepository
import cz.mendelu.pef.database.LocalTasksRepositoryImpl
import cz.mendelu.pef.database.TasksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTasksRepository(dao: TasksDao): ILocalTasksRepository{
        return LocalTasksRepositoryImpl(dao)
    }
}