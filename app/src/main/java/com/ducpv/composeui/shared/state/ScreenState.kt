package com.ducpv.composeui.shared.state

/**
 * Created by pvduc9773 on 11/06/2023.
 */
sealed class ScreenState {
    object Idle : ScreenState()
    object Progressing : ScreenState()
    data class Completed(val message: String? = null) : ScreenState()
    data class Failed(val message: String? = null) : ScreenState()
}
