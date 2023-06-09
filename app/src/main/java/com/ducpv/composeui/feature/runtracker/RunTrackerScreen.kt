package com.ducpv.composeui.feature.runtracker

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import com.ducpv.composeui.domain.model.RunTracker
import com.ducpv.composeui.domain.model.toLocation
import com.ducpv.composeui.domain.service.RunTrackingService
import com.ducpv.composeui.domain.service.TrackingState
import com.ducpv.composeui.feature.tictactoegame.shapeBackground
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.color
import com.ducpv.composeui.shared.utility.GoogleMapUtility
import com.ducpv.composeui.shared.utility.PermissionUtility
import com.ducpv.composeui.shared.utility.millisecondToTimeFormat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
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
    viewModel: RunTrackerViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val currentLocation = viewModel.currentLocation.collectAsState()
    val trackingState = viewModel.trackingState.collectAsState()
    val pathPoints = viewModel.pathPoints.collectAsState()
    val runTime = viewModel.runTime.collectAsState()
    val runTrackerList = viewModel.runTrackerList.collectAsState()

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
        currentLocation.value?.let {
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
        if (permissionsState.allPermissionsGranted) {
            RunTrackingService.onRequestCurrentLocation(context)
        } else {
            Timber.d("/// onLaunchMultiplePermissionRequest")
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.currentLocation.collect { currentLocation ->
            if (currentLocation == null) return@collect
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(currentLocation, GoogleMapUtility.zoomSizeDefault, 0f, 0f),
                ),
                durationMs = 600,
            )
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
                points = pathPoints.value,
                color = ThemeColor.Red.color,
                clickable = true,
                width = 8f,
            )
            currentLocation.value?.let { currentLocation ->
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
                    Text(text = "Lat: ${currentLocation.value?.latitude ?: "N/A"}")
                    Text(text = "Lng: ${currentLocation.value?.longitude ?: "N/A"}")
                    if (runTime.value > 0) {
                        Text(text = runTime.value.millisecondToTimeFormat())
                    }
                }

                Column {
                    Button(onClick = {
                        when (trackingState.value) {
                            TrackingState.NONE,
                            TrackingState.PAUSED,
                            TrackingState.STOPPED -> {
                                RunTrackingService.onStartService(context)
                            }
                            TrackingState.RUNNING -> {
                                RunTrackingService.onPauseService(context)
                            }
                        }
                    }) {
                        Text(text = trackingState.value.actionName)
                    }
                    if (trackingState.value == TrackingState.RUNNING) {
                        Button(onClick = {
                            RunTrackingService.onStopService(context)
                        }) {
                            Text(text = "Stop")
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            if (runTrackerList.value.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    runTrackerList.value.forEach { runTracker ->
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
    val points = remember {
        runTracker.points.map { it.toLocation() }
    }
    val endPoint = remember {
        points.lastOrNull() ?: LatLng(0.0, 0.0)
    }

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
        position = CameraPosition.fromLatLngZoom(endPoint, GoogleMapUtility.zoomSizeDefault * 1.2f)
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
                points = points,
                color = ThemeColor.Red.color,
                clickable = true,
                width = 8f,
            )
        }
    }
}
