package com.ducpv.composeui.feature.chat.authentication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ducpv.composeui.R
import com.ducpv.composeui.domain.usecase.SignInUseCase
import com.ducpv.composeui.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 26/05/2023.
 */
enum class AuthenticationType(val typeId: String) {
    SIGN_IN("signIn"),
    SIGN_UP("signUp");

    val displayName: Int
        get() = when (this) {
            SIGN_IN -> R.string.sign_in
            SIGN_UP -> R.string.sign_up
        }

    companion object {
        fun getTypeById(typeId: String?): AuthenticationType {
            return values().find { it.typeId == typeId } ?: SIGN_IN
        }
    }
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
) : ViewModel() {
    val authenticationType: AuthenticationType = AuthenticationType.getTypeById(savedStateHandle.get<String>("typeId"))

    fun onButtonClick(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit,
    ) {
        when (authenticationType) {
            AuthenticationType.SIGN_IN -> onSignIn(email, password, onSuccess, onFailed)
            AuthenticationType.SIGN_UP -> onSignUp(email, password, onSuccess, onFailed)
        }
    }

    private fun onSignUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                signUpUseCase.invoke(email, password)
                onSuccess.invoke()
            } catch (e: Exception) {
                onFailed.invoke(e.message.toString())
            }
        }
    }

    private fun onSignIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                signInUseCase.invoke(email, password)
                onSuccess.invoke()
            } catch (e: Exception) {
                onFailed.invoke(e.message.toString())
            }
        }
    }
}
