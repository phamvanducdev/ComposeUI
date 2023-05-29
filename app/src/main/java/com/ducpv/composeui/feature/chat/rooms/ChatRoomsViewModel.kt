package com.ducpv.composeui.feature.chat.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.ducpv.composeui.domain.model.ChatRoom
import com.ducpv.composeui.domain.model.RoomType
import com.ducpv.composeui.domain.model.UserInfo
import com.ducpv.composeui.domain.usecase.ChatRoomListUseCase
import com.ducpv.composeui.domain.usecase.SignInUseCase
import com.ducpv.composeui.domain.usecase.SignOutUseCase
import com.ducpv.composeui.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 23/05/2023.
 */
data class UiState(
    val userInfo: UserInfo? = null,
    val chatRooms: List<ChatRoom> = emptyList()
)

@HiltViewModel
class ChatRoomsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val chatRoomsUserCase: ChatRoomListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState(),
        )

    private var fetchChatRoomsJob: Job? = null

    init {
        viewModelScope.launch {
            authDataStore.userInfo
                .distinctUntilChanged()
                .collect { userInfo ->
                    _uiState.update { state ->
                        state.copy(userInfo = userInfo)
                    }
                    fetchChatRooms()
                }
        }
    }

    private fun fetchChatRooms() {
        val previousJob = fetchChatRoomsJob
        fetchChatRoomsJob = viewModelScope.launch {
            previousJob?.cancelAndJoin()
            chatRoomsUserCase.invoke(roomType = RoomType.PUBLIC).collect { chatRooms ->
                _uiState.update { state ->
                    state.copy(chatRooms = chatRooms)
                }
            }
        }
    }

    fun onSignUp(
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

    fun onSignIn(
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

    fun onLogout() {
        viewModelScope.launch {
            signOutUseCase.invoke()
        }
    }
}
