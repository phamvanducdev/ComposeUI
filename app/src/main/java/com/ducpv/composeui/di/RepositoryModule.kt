package com.ducpv.composeui.di

import com.ducpv.composeui.domain.database.dao.RunTrackerDao
import com.ducpv.composeui.domain.repository.RunTrackerRepository
import com.ducpv.composeui.domain.repository.RunTrackerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by pvduc9773 on 15/05/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun providesRunTrackerRepository(runTrackerDao: RunTrackerDao): RunTrackerRepository {
        return RunTrackerRepositoryImpl(runTrackerDao)
    }
}
