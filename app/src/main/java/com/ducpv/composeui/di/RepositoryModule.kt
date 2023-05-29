package com.ducpv.composeui.di

import com.ducpv.composeui.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by pvduc9773 on 15/05/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun providesRunTrackerRepository(
        runTrackerRepository: RunTrackerRepositoryImpl
    ): RunTrackerRepository

    @Binds
    fun providesAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    fun providesFireStoreRepository(
        fireStoreRepository: FireStoreRepositoryImpl
    ): FireStoreRepository
}
