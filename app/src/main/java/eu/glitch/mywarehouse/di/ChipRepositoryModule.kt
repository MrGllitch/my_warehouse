package eu.glitch.mywarehouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.glitch.mywarehouse.database.chip.ChipDao
import eu.glitch.mywarehouse.database.chip.ILocalChipRepository
import eu.glitch.mywarehouse.database.chip.LocalChipRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChipRepositoryModule {
    @Provides
    @Singleton
    fun provideTypeRepository(chipDao: ChipDao): ILocalChipRepository {
        return LocalChipRepositoryImpl(chipDao)
    }
}
