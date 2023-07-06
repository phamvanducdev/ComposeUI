package com.ducpv.composeui.di

import android.content.Context
import com.ducpv.composeui.domain.database.RunTrackerDatabase
import com.ducpv.composeui.domain.database.dao.RunTrackerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by pvduc9773 on 11/05/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun providesRunTrackerDatabase(@ApplicationContext context: Context): RunTrackerDatabase =
        RunTrackerDatabase.buildDatabase(context)

    @Provides
    fun providesMovieDao(database: RunTrackerDatabase): RunTrackerDao = database.runTrackerDao()
}
