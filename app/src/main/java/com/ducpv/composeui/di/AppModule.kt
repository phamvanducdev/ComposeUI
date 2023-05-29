package com.ducpv.composeui.di

import com.ducpv.composeui.BuildConfig
import com.ducpv.composeui.core.util.AppConfig
import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.core.util.AppDispatcherImpl
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
class AppModule {
    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig = AppConfig(
        BuildConfig.VERSION_CODE,
        BuildConfig.VERSION_NAME,
    )

    @Provides
    @Singleton
    fun provideAppDispatcher(): AppDispatcher = AppDispatcherImpl()
}
