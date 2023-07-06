package com.ducpv.composeui.feature.chat.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ducpv.composeui.domain.model.chat.MessageType
import com.ducpv.composeui.domain.model.chat.Room
import com.ducpv.composeui.domain.usecase.chat.GetRoomInfoUseCase
import com.ducpv.composeui.domain.usecase.chat.MessageItem
import com.ducpv.composeui.domain.usecase.chat.SendMessageUseCase
import com.ducpv.composeui.domain.usecase.chat.SubscribeMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 17/06/2023.
 */
@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRoomInfoUseCase: GetRoomInfoUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val subscribeMessagesUseCase: SubscribeMessagesUseCase,
) : ViewModel() {
    private val rid: String = savedStateHandle.get<String>("rid").orEmpty()

    val roomInfo: Flow<Room?> = getRoomInfoUseCase.roomInfo
    val messageList: Flow<List<MessageItem>> = subscribeMessagesUseCase.subscribeMessages

    init {
        viewModelScope.launch {
            getRoomInfoUseCase.invoke(rid)
            subscribeMessagesUseCase.startSubscribeMessage(rid = rid)
        }
    }

    override fun onCleared() {
        subscribeMessagesUseCase.removeSubscribeMessages()
        super.onCleared()
    }

    fun sendTextMessage(content: String) {
        sendMessage(content, MessageType.Text)
    }

    fun sendImageMessage(url: String) {
        // TODO
        sendMessage(url, MessageType.Image)
    }

    private fun sendMessage(
        content: String,
        type: MessageType
    ) {
        viewModelScope.launch {
            sendMessageUseCase.invoke(
                rid = rid,
                content = content,
                type = type,
            )
        }
    }
}
