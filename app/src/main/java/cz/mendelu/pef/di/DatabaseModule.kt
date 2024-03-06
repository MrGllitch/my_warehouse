package cz.mendelu.pef.di

import android.content.Context
import cz.mendelu.pef.database.ILocalTasksRepository
import cz.mendelu.pef.database.LocalTasksRepositoryImpl
import cz.mendelu.pef.database.TasksDao
import cz.mendelu.pef.database.TasksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TasksDatabase{
        return TasksDatabase.getDatabase(context = context)
    }
}