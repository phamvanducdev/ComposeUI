package com.ducpv.composeui.feature.runtracker

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ducpv.composeui.R
import com.ducpv.composeui.domain.model.RunTracker
import com.ducpv.composeui.domain.model.toLocation
import com.ducpv.composeui.domain.service.RunTrackingService
import com.ducpv.composeui.domain.service.TrackingState
import com.ducpv.composeui.domain.service.millisecondToRunTimeFormat
import com.ducpv.composeui.feature.tictactoegame.shapeBackground
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.navigation.NavigationIcon
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.color
import com.ducpv.composeui.shared.utility.GoogleMapUtility
import com.ducpv.composeui.shared.utility.PermissionUtility
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import timber.log.Timber

/**
 * Created by pvduc9773 on 08/05/2023.
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
)
@Composable
fun RunTrackerScreen(
    appState: AppState,
    viewModel: RunTrackerViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        appState.topBarTitle = R.string.run_tracker
        appState.navigationIcon = NavigationIcon.Menu
    }

    val context: Context = LocalContext.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val currentLocation by RunTrackingService.currentLocation.collectAsStateWithLifecycle()
    val trackingState by RunTrackingService.trackingState.collectAsStateWithLifecycle()
    val pathPoints by RunTrackingService.pathPoints.collectAsStateWithLifecycle()
    val runTime by RunTrackingService.runTime.collectAsStateWithLifecycle()
    val runTrackerList by viewModel.runTrackerList.collectAsStateWithLifecycle(initialValue = emptyList())

    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
            ),
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        currentLocation?.let {
            position = CameraPosition.fromLatLngZoom(it, GoogleMapUtility.zoomSizeDefault)
        }
    }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = PermissionUtility.runTrackingPermissions,
        onPermissionsResult = { permissionsResult ->
            Timber.d("/// onPermissionsResult: $permissionsResult")
            if (PermissionUtility.permissionsGranted(permissionsResult)) {
                RunTrackingService.onRequestCurrentLocation(context)
            }
        },
    )

    LaunchedEffect(Unit) {
        RunTrackingService.currentLocation.collect { currentLocation ->
            if (currentLocation == null) return@collect
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(currentLocation, GoogleMapUtility.zoomSizeDefault, 0f, 0f),
                ),
                durationMs = 600,
            )
        }
    }

    LaunchedEffect(Unit) {
        MapsInitializer.initialize(context, MapsInitializer.Renderer.LATEST) {
            Timber.d("/// onMapsSdkInitializedCallback: $it")
        }
    }

    LaunchedEffect(Unit) {
        if (permissionsState.allPermissionsGranted) {
            RunTrackingService.onRequestCurrentLocation(context)
        } else {
            Timber.d("/// onLaunchMultiplePermissionRequest")
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
        ) {
            Polyline(
                points = pathPoints,
                color = ThemeColor.Red.color,
                clickable = true,
                width = 8f,
            )
            currentLocation?.let { currentLocation ->
                Marker(
                    state = MarkerState(
                        position = currentLocation,
                    ),
                    alpha = 0.6f,
                )
            }
        }

        if (permissionsState.allPermissionsGranted) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(text = "Lat: ${currentLocation?.latitude ?: "N/A"}")
                    Text(text = "Lng: ${currentLocation?.longitude ?: "N/A"}")
                    if (runTime > 0) {
                        Text(text = runTime.millisecondToRunTimeFormat())
                    }
                }

                Column {
                    Button(onClick = {
                        when (trackingState) {
                            TrackingState.NONE,
                            TrackingState.STOPPED -> {
                                RunTrackingService.onStartService(context)
                            }
                            TrackingState.PAUSED -> {
                                RunTrackingService.onResumeService(context)
                            }
                            TrackingState.RUNNING -> {
                                RunTrackingService.onPauseService(context)
                            }
                        }
                    }) {
                        Text(text = trackingState.actionName)
                    }
                    if (trackingState == TrackingState.RUNNING) {
                        Button(onClick = {
                            RunTrackingService.onStopService(context)
                        }) {
                            Text(text = "Stop")
                        }
                    }
                }
            }
        }

        /**
         * TODO issue crash app
         * java.lang.NullPointerException: Attempt to get length of null array
         * at java.nio.ByteBufferAsIntBuffer.put(ByteBufferAsIntBuffer.java:122)
         * at com.google.maps.api.android.lib6.gmm6.vector.gl.buffer.n.i(:com.google.android.gms.dynamite_mapsdynamite@231613045@23.16.13 (190408-0):2)
         * at com.google.maps.api.android.lib6.gmm6.vector.gl.buffer.n.d(:com.google.android.gms.dynamite_mapsdynamite@231613045@23.16.13 (190408-0):3)
         * at com.google.maps.api.android.lib6.gmm6.vector.gl.drawable.d.s(:com.google.android.gms.dynamite_mapsdynamite@231613045@23.16.13 (190408-0):2)
         * at com.google.maps.api.android.lib6.gmm6.vector.gl.drawable.ao.s(:com.google.android.gms.dynamite_mapsdynamite@231613045@23.16.13 (190408-0):12)
         * at com.google.maps.api.android.lib6.gmm6.vector.bx.s(:com.google.android.gms.dynamite_mapsdynamite@231613045@23.16.13 (190408-0):29)
         * at com.google.maps.api.android.lib6.gmm6.vector.bq.b(:com.google.android.gms.dynamite_mapsdynamite@231613045@23.16.13 (190408-0):151)
         * at com.google.maps.api.android.lib6.gmm6.vector.at.run(:com.google.android.gms.dynamite_mapsdynamite@231613045@23.16.13 (190408-0):48)
         */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            if (runTrackerList.isNotEmpty()) {
                val rowState = rememberLazyListState()
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    state = rowState,
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    runTrackerList.forEach { runTracker ->
                        item {
                            ItemRunTracker(
                                runTracker = runTracker,
                                itemWidth = (screenWidthDp * 0.6).dp,
                                itemHeight = (screenWidthDp * 0.4).dp,
                                onItemClickListener = {
                                    // TODO
                                },
                            )
                        }
                    }
                }
            }

            if (!permissionsState.allPermissionsGranted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    FloatingActionButton(
                        modifier = Modifier
                            .padding(16.dp)
                            .shapeBackground(ThemeColor.Red.color),
                        contentColor = ThemeColor.White.color,
                        containerColor = ThemeColor.Red.color,
                        onClick = {
                            permissionsState.launchMultiplePermissionRequest()
                        },
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Request permissions")
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ItemRunTracker(
    runTracker: RunTracker,
    itemWidth: Dp,
    itemHeight: Dp,
    onItemClickListener: (RunTracker) -> Unit
) {
    if (runTracker.points.isEmpty()) return

    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                zoomGesturesEnabled = false,
                scrollGesturesEnabled = false,
            ),
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            runTracker.points.map { it.toLocation() }.lastOrNull() ?: LatLng(0.0, 0.0),
            GoogleMapUtility.zoomSizeDefault * 1.2f,
        )
    }

    Card(
        modifier = Modifier.size(
            width = itemWidth,
            height = itemHeight,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = ThemeColor.Gray.color,
        ),
        onClick = { onItemClickListener.invoke(runTracker) },
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
        ) {
            Polyline(
                points = runTracker.points.map { it.toLocation() },
                color = ThemeColor.Red.color,
                clickable = true,
                width = 8f,
            )
        }
    }
}
