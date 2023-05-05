package com.ducpv.composeui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 09/04/2023.
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    sealed class MainAppState {
        object Idle : MainAppState()
        object Loading : MainAppState()
        object Completed : MainAppState()
        object Failed : MainAppState()
    }

    private val _state = MutableStateFlow<MainAppState>(MainAppState.Idle)
    val state = _state.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            _state.value = MainAppState.Loading
            // TODO initialize app config
            delay(1.seconds)
            _state.value = MainAppState.Completed
        }
    }
}
