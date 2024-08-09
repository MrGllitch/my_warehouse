package eu.glitch.mywarehouse.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.database.LocalRepositoryImpl
import eu.glitch.mywarehouse.database.WarehouseDatabase
import eu.glitch.mywarehouse.database.unit.UnitDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    //@Provides
    //@Singleton
    //fun provideDatabase(@ApplicationContext context: Context): WarehouseDatabase {
    //    return WarehouseDatabase.getDatabase(context = context)
    //}

    @Provides
    @Singleton
    fun provideLocalRepository(repository: LocalRepositoryImpl) : IRepository{
        return repository
    }

    /// Daos

    //@Provides
    //@Singleton
    //fun provideDao(database: WarehouseDatabase): UnitDao {
    //    return database.UnitDao()
    //}


}