package com.ducpv.composeui.feature.chat.rooms

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.ducpv.composeui.domain.datastore.toUser
import com.ducpv.composeui.domain.model.chat.Room
import com.ducpv.composeui.domain.model.chat.User
import com.ducpv.composeui.domain.usecase.auth.SignOutUseCase
import com.ducpv.composeui.domain.usecase.auth.UploadImageUserAvatarUseCase
import com.ducpv.composeui.domain.usecase.chat.SubscribeRoomsUseCase
import com.ducpv.composeui.domain.usecase.chat.SubscribeRoomsUseCase.Companion.SubscribeRoomsType
import com.ducpv.composeui.shared.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 23/05/2023.
 */
@HiltViewModel
class ChatRoomsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val signOutUseCase: SignOutUseCase,
    private val subscribeRoomsUseCase: SubscribeRoomsUseCase,
    private val uploadImageUserAvatarUseCase: UploadImageUserAvatarUseCase,
) : ViewModel() {
    val screenState = mutableStateOf<ScreenState>(ScreenState.Idle)

    val roomList: Flow<List<Room>> = subscribeRoomsUseCase.subscribeRooms
    val userInfo: Flow<User?> = authDataStore.subscribeUser.map { dataStoreUser ->
        dataStoreUser?.toUser()
    }

    init {
        viewModelScope.launch {
            authDataStore.subscribeUser
                .distinctUntilChanged()
                .collect { dataStoreUser ->
                    if (dataStoreUser != null) {
                        subscribeRoomsUseCase.startSubscribeRooms(SubscribeRoomsType.All)
                    } else {
                        subscribeRoomsUseCase.removeSubscribeRooms()
                    }
                }
        }
    }

    fun onJoinRoom(rid: String) {
        // TODO
    }

    fun onChangeImage(
        imageUri: Uri?,
        onSuccess: () -> Unit
    ) {
        if (imageUri == null) {
            screenState.value = ScreenState.Failed(
                Exception("Not found image info!").message.toString(),
            )
            return
        }
        viewModelScope.launch {
            try {
                screenState.value = ScreenState.Progressing
                uploadImageUserAvatarUseCase(imageUri = imageUri)
                onSuccess.invoke()
            } catch (e: Exception) {
                screenState.value = ScreenState.Failed(e.message.toString())
            }
        }
    }

    fun onLogout(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            signOutUseCase()
            onSuccess.invoke()
        }
    }

    fun clearScreenState() {
        screenState.value = ScreenState.Idle
    }
}
