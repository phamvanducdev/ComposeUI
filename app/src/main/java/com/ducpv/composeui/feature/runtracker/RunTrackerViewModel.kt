package com.ducpv.composeui.feature.runtracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ducpv.composeui.domain.model.RunTracker
import com.ducpv.composeui.domain.service.RunTrackingService
import com.ducpv.composeui.domain.usecase.RunTrackerListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 09/05/2023.
 */
@HiltViewModel
class RunTrackerViewModel @Inject constructor(
    private val runTrackerListUseCase: RunTrackerListUseCase
) : ViewModel() {
    val currentLocation = RunTrackingService.currentLocation.asStateFlow()
    val trackingState = RunTrackingService.trackingState.asStateFlow()
    val pathPoints = RunTrackingService.pathPoints.asStateFlow()
    val runTime = RunTrackingService.runTime.asStateFlow()

    private val _runTrackerList = MutableStateFlow<List<RunTracker>>(emptyList())
    val runTrackerList: StateFlow<List<RunTracker>> = _runTrackerList.asStateFlow()

    init {
        viewModelScope.launch {
            runTrackerListUseCase().collect { runTrackers ->
                _runTrackerList.value = runTrackers
            }
        }
    }
}
