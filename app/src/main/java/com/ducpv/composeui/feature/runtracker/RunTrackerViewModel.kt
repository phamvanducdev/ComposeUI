package com.ducpv.composeui.feature.runtracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ducpv.composeui.domain.model.RunTracker
import com.ducpv.composeui.domain.usecase.runtracker.SubscribeRunTrackerListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 09/05/2023.
 */
@HiltViewModel
class RunTrackerViewModel @Inject constructor(
    private val subscribeRunTrackerListUseCase: SubscribeRunTrackerListUseCase
) : ViewModel() {
    private val _runTrackerList = MutableStateFlow<List<RunTracker>>(emptyList())
    val runTrackerList: Flow<List<RunTracker>> = _runTrackerList

    init {
        viewModelScope.launch {
            subscribeRunTrackerListUseCase().collect { runTrackers ->
                _runTrackerList.value = runTrackers
            }
        }
    }
}
