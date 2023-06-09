package com.ducpv.composeui.di

import com.ducpv.composeui.BuildConfig
import com.ducpv.composeui.core.util.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by pvduc9773 on 07/04/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
class UtilModule {
    @Singleton
    @Provides
    fun provideAppConfig(): AppConfig =
        AppConfig(
            BuildConfig.VERSION_CODE,
            BuildConfig.VERSION_NAME,
        )
}
