package com.ducpv.composeui.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ducpv.composeui.MainActivity
import com.ducpv.composeui.R
import com.ducpv.composeui.domain.service.RunTrackingService
import com.ducpv.composeui.domain.service.RunTrackingService.Companion.NOTIFICATION_CHANNEL_ID
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

/**
 * Created by pvduc9773 on 10/05/2023.
 */
@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun providesFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @ServiceScoped
    @Provides
    fun providesMainActivityPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java).apply {
            action = RunTrackingService.ACTION_OPEN_RUN_TRACKER
        },
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    @ServiceScoped
    @Provides
    fun providesNotificationCompatBuilder(
        @ApplicationContext context: Context,
        mainActivityPendingIntent: PendingIntent
    ): NotificationCompat.Builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_component)
        .setContentTitle(context.getString(R.string.run_tracker))
        .setContentText("00:00:00")
        .setContentIntent(mainActivityPendingIntent)
}
