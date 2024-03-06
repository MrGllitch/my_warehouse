package cz.mendelu.pef.di

import cz.mendelu.pef.database.ILocalTasksRepository
import cz.mendelu.pef.database.LocalTasksRepositoryImpl
import cz.mendelu.pef.database.TasksDao
import cz.mendelu.pef.database.TasksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideDao(database: TasksDatabase): TasksDao{
        return database.tasksDao()
    }
}