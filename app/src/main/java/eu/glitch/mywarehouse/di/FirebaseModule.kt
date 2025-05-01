package eu.glitch.mywarehouse.di

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.impl.AccountServiceImpl
import eu.glitch.mywarehouse.database.impl.FirestoreRepositoryImpl
import eu.glitch.mywarehouse.database.IAccountService
import eu.glitch.mywarehouse.database.IRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideStorage() : FirebaseStorage = Firebase.storage



    @Provides
    @Singleton
    fun provideFirebaseRepository(firestoreDatabase: FirestoreRepositoryImpl): IRepository {
        return firestoreDatabase
    }

    @Provides
    @Singleton
    fun provideAccountService(accountService: AccountServiceImpl): IAccountService {
        return accountService
    }

}