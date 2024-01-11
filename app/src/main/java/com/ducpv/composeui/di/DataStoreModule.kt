package com.ducpv.composeui.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.ducpv.composeui.domain.datastore.AuthDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by pvduc9773 on 24/05/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    @Singleton
    fun providesAuthDataStore(
        @ApplicationContext context: Context
    ): AuthDataStore {
        return AuthDataStoreImpl(
            dataStore = PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile("auth_datastore")
            },
        )
    }
}
