package com.ducpv.composeui.domain.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.ducpv.composeui.R
import com.ducpv.composeui.domain.database.dao.RunTrackerDao
import com.ducpv.composeui.domain.database.entity.PointEntity
import com.ducpv.composeui.domain.database.entity.RunTrackerEntity
import com.ducpv.composeui.shared.utility.PermissionUtility
import com.ducpv.composeui.shared.utility.millisecondToTimeFormat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

/**
 * Created by pvduc9773 on 09/05/2023.
 */
enum class TrackingState {
    NONE, RUNNING, PAUSED, STOPPED;

    val actionName: String
        get() = when (this) {
            NONE,
            STOPPED -> "Start"
            PAUSED -> "Resume"
            RUNNING -> "Pause"
        }
}

enum class NotificationActionType {
    START, RESUME, PAUSE, STOP;

    val actionName: String
        get() = when (this) {
            START -> "Start"
            RESUME -> "Resume"
            PAUSE -> "Pause"
            STOP -> "Stop"
        }

    val actionIcon: Int
        get() = when (this) {
            START -> R.drawable.ic_play
            RESUME -> R.drawable.ic_play
            PAUSE -> R.drawable.ic_pause
            STOP -> R.drawable.ic_stop
        }

    val actionIntent: String
        get() = when (this) {
            START -> RunTrackingService.ACTION_START_OR_RESUME_SERVICE
            RESUME -> RunTrackingService.ACTION_START_OR_RESUME_SERVICE
            PAUSE -> RunTrackingService.ACTION_PAUSE_SERVICE
            STOP -> RunTrackingService.ACTION_STOP_SERVICE
        }
}

@AndroidEntryPoint
class RunTrackingService : LifecycleService() {
    companion object {
        val currentLocation = MutableStateFlow<LatLng?>(null)
        val trackingState = MutableStateFlow(TrackingState.NONE)
        val pathPoints = MutableStateFlow(mutableListOf<LatLng>())
        val runTime = MutableStateFlow(0L)

        const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
        const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
        const val ACTION_REQUEST_CURRENT_LOCATION = "ACTION_REQUEST_CURRENT_LOCATION"

        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "running_tracker"
        const val NOTIFICATION_CHANNEL_NAME = "RunTracker"

        const val LOCATION_UPDATE_INTERVAL = 10_000L

        fun onRequestCurrentLocation(context: Context) {
            context.startService(
                Intent(context, RunTrackingService::class.java).apply {
                    action = ACTION_REQUEST_CURRENT_LOCATION
                },
            )
        }

        fun onStartService(context: Context) {
            context.startService(
                Intent(context, RunTrackingService::class.java).apply {
                    action = ACTION_START_OR_RESUME_SERVICE
                },
            )
        }

        fun onPauseService(context: Context) {
            context.startService(
                Intent(context, RunTrackingService::class.java).apply {
                    action = ACTION_PAUSE_SERVICE
                },
            )
        }

        fun onStopService(context: Context) {
            context.startService(
                Intent(context, RunTrackingService::class.java).apply {
                    action = ACTION_STOP_SERVICE
                },
            )
        }
    }

    @Inject
    lateinit var runTrackerDao: RunTrackerDao

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManager

    private var timerJob: Job? = null
    private var trackingJob: Job? = null
    private var notificationJob: Job? = null
    private var requestCurrentLocationJob: Job? = null

    private var isTimerRunning = false
    private var startTime = 0L

    private val requestLocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            for (location in p0.locations) {
                if (trackingState.value == TrackingState.RUNNING) {
                    val points = pathPoints.value.toMutableList().apply {
                        add(LatLng(location.latitude, location.longitude))
                    }
                    pathPoints.value = points
                }
            }
            Timber.d("/// pathPoints: ${pathPoints.value}")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_REQUEST_CURRENT_LOCATION -> {
                requestCurrentLocation()
            }
            ACTION_START_OR_RESUME_SERVICE -> {
                if (trackingState.value == TrackingState.PAUSED) {
                    resumeTracking()
                } else {
                    startTracking()
                }
            }
            ACTION_PAUSE_SERVICE -> {
                pauseTracking()
            }
            ACTION_STOP_SERVICE -> {
                onStopService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation() {
        if (PermissionUtility.hasPermissions(this, PermissionUtility.runTrackingPermissions)) {
            requestCurrentLocationJob = SupervisorJob()
            requestCurrentLocationJob?.let { requestCurrentLocationJob ->
                CoroutineScope(Dispatchers.IO + requestCurrentLocationJob).launch {
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { position ->
                        Timber.d("/// fusedLocationProviderClient.addOnSuccessListener: $position")
                        if (position != null) {
                            currentLocation.value = LatLng(position.latitude, position.longitude)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationBuilder = baseNotificationBuilder
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        trackingJob = SupervisorJob()
        trackingJob?.let { trackingJob ->
            CoroutineScope(Dispatchers.IO + trackingJob).launch {
                trackingState.collect {
                    updateLocationTracking(it)
                    updateNotificationTracking(it)
                }
            }
        }
    }

    override fun onDestroy() {
        stopTracking()
        stopTimer()
        super.onDestroy()
    }

    private fun onStopService() {
        updateTrackingDatabase(
            points = pathPoints.value,
            runTime = runTime.value,
            startTime = startTime,
        )
        stopTracking()
        stopTimer()
        stopForegroundService()
    }

    private fun startTracking() {
        trackingState.value = TrackingState.RUNNING
        pathPoints.value = mutableListOf()
        startForegroundService()
        startTimer()
    }

    private fun resumeTracking() {
        trackingState.value = TrackingState.RUNNING
        startForegroundService()
        resumeTimer()
    }

    private fun pauseTracking() {
        trackingState.value = TrackingState.PAUSED
        pauseTimer()
    }

    private fun stopTracking() {
        trackingState.value = TrackingState.STOPPED
        pathPoints.value = mutableListOf()
        trackingJob?.cancel()
        trackingJob = null
        requestCurrentLocationJob?.cancel()
        requestCurrentLocationJob = null
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis()
        isTimerRunning = true
        timerJob = SupervisorJob()
        timerJob?.let { timerJob ->
            CoroutineScope(Dispatchers.IO + timerJob).launch {
                while (true) {
                    if (isTimerRunning) {
                        runTime.value = runTime.value + 1000L
                    }
                    delay(1000L)
                }
            }
        }
    }

    private fun resumeTimer() {
        isTimerRunning = true
    }

    private fun pauseTimer() {
        isTimerRunning = false
    }

    private fun stopTimer() {
        isTimerRunning = false
        runTime.value = 0L
        timerJob?.cancel()
        timerJob = null
    }

    private fun startForegroundService() {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW,
            ),
        )
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
        notificationJob = SupervisorJob()
        notificationJob?.let { notificationJob ->
            CoroutineScope(Dispatchers.IO + notificationJob).launch {
                runTime.collect {
                    val notification = notificationBuilder.setContentText(it.millisecondToTimeFormat())
                    notificationManager.notify(NOTIFICATION_ID, notification.build())
                }
            }
        }
    }

    private fun stopForegroundService() {
        notificationJob?.cancel()
        notificationJob = null
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun updateTrackingDatabase(
        points: List<LatLng>,
        runTime: Long,
        startTime: Long
    ) {
        if (points.size > 10) return
        CoroutineScope(Dispatchers.IO).launch {
            val runTracker = RunTrackerEntity(
                startTime = startTime,
                endTime = System.currentTimeMillis(),
                runTime = runTime,
                points = points.map {
                    PointEntity(
                        latitude = it.latitude,
                        longitude = it.longitude,
                    )
                },
            )
            runTrackerDao.insertRunTracker(runTracker)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(trackingState: TrackingState) {
        if (trackingState == TrackingState.RUNNING) {
            if (PermissionUtility.hasPermissions(this, PermissionUtility.runTrackingPermissions)) {
                fusedLocationProviderClient.requestLocationUpdates(
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_UPDATE_INTERVAL).build(),
                    requestLocationCallback,
                    Looper.getMainLooper(),
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(requestLocationCallback)
        }
    }

    private fun updateNotificationTracking(trackingState: TrackingState) {
        notificationBuilder.clearActions()
        createNotificationTrackingActions(trackingState).forEach { action ->
            notificationBuilder.addAction(
                action.actionIcon,
                action.actionName,
                createNotificationTrackingActionPendingIntent(action),
            )
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationTrackingActions(trackingState: TrackingState): List<NotificationActionType> {
        return when (trackingState) {
            TrackingState.NONE -> listOf(
                NotificationActionType.START,
            )
            TrackingState.RUNNING -> listOf(
                NotificationActionType.PAUSE,
                NotificationActionType.STOP,
            )
            TrackingState.PAUSED -> listOf(
                NotificationActionType.RESUME,
                NotificationActionType.STOP,
            )
            else -> emptyList()
        }
    }

    private fun createNotificationTrackingActionPendingIntent(
        notificationActionType: NotificationActionType
    ): PendingIntent {
        return PendingIntent.getService(
            this,
            0,
            Intent(this, RunTrackingService::class.java).apply {
                action = notificationActionType.actionIntent
            },
            FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE,
        )
    }
}
