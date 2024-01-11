package com.ducpv.composeui.domain.usecase.chat

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.ducpv.composeui.domain.firestore.model.toMessage
import com.ducpv.composeui.domain.firestore.model.toUser
import com.ducpv.composeui.domain.model.chat.Message
import com.ducpv.composeui.domain.model.chat.MessageType
import com.ducpv.composeui.domain.model.chat.User
import com.ducpv.composeui.domain.repository.FireStoreRepository
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 15/05/2023.
 */
data class MessageItem(
    val type: MessageType,
    val content: String,
    val author: User,
    val createdAt: Date,
    val isMine: Boolean,
)

fun Message.toMessageUi(
    author: User,
    mineId: String?,
): MessageItem =
    MessageItem(
        type = this.type,
        content = this.content,
        author = author,
        createdAt = this.createdAt,
        isMine = this.uid == mineId,
    )

class SubscribeMessagesUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val authDataStore: AuthDataStore,
    private val fireStoreRepository: FireStoreRepository,
) {
    private var subscribeMessagesScope: CoroutineScope? = null

    private val _subscribeMessages: MutableStateFlow<List<MessageItem>> = MutableStateFlow(emptyList())
    val subscribeMessages: Flow<List<MessageItem>>
        get() = _subscribeMessages

    suspend fun startSubscribeMessage(rid: String, limit: Long = 100L) {
        val mineId = authDataStore.getUserInfo()?.uid
        val room = fireStoreRepository.getRoom(rid) ?: return
        val authors = fireStoreRepository.getUsers(room.uids).map { it.toUser() }
        subscribeMessagesScope = CoroutineScope(SupervisorJob() + dispatcher.io)
        subscribeMessagesScope?.launch {
            fireStoreRepository.subscribeMessages(rid, limit).collect { fireStoreMessages ->
                _subscribeMessages.emit(
                    fireStoreMessages
                        .map { it.toMessage() }
                        .mapNotNull { message ->
                            val author = authors.find {
                                it.uid == message.uid
                            } ?: return@mapNotNull null
                            message.toMessageUi(
                                author = author,
                                mineId = mineId,
                            )
                        },
                )
            }
        }
    }

    fun removeSubscribeMessages() {
        subscribeMessagesScope?.launch {
            _subscribeMessages.emit(emptyList())
        }
        subscribeMessagesScope?.cancel()
        subscribeMessagesScope = null
    }
}
