package com.ducpv.composeui.feature.chat.authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ducpv.composeui.R
import com.ducpv.composeui.domain.usecase.auth.SignInUseCase
import com.ducpv.composeui.domain.usecase.auth.SignUpUseCase
import com.ducpv.composeui.shared.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 26/05/2023.
 */
enum class AuthenticationType(val value: String) {
    SIGN_IN("signIn"),
    SIGN_UP("signUp");

    val displayName: Int
        get() = when (this) {
            SIGN_IN -> R.string.sign_in
            SIGN_UP -> R.string.sign_up
        }

    companion object {
        fun convertBy(type: String?): AuthenticationType {
            return values().find { it.value == type } ?: SIGN_IN
        }
    }
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
) : ViewModel() {
    val authenticationType: AuthenticationType = AuthenticationType.convertBy(savedStateHandle.get<String>("type"))

    val screenState = mutableStateOf<ScreenState>(ScreenState.Idle)

    fun onButtonClick(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        when (authenticationType) {
            AuthenticationType.SIGN_IN -> onSignIn(email, password, onSuccess)
            AuthenticationType.SIGN_UP -> onSignUp(email, password, onSuccess)
        }
    }

    private fun onSignUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                screenState.value = ScreenState.Progressing
                signUpUseCase.invoke(email, password)
                onSuccess.invoke()
            } catch (e: Exception) {
                screenState.value = ScreenState.Failed(e.message.toString())
            }
        }
    }

    private fun onSignIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                screenState.value = ScreenState.Progressing
                signInUseCase.invoke(email, password)
                onSuccess.invoke()
            } catch (e: Exception) {
                screenState.value = ScreenState.Failed(e.message.toString())
            }
        }
    }

    fun clearScreenState() {
        screenState.value = ScreenState.Idle
    }
}
