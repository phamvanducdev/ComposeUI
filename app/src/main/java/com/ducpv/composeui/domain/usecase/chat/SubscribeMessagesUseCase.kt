package com.ducpv.composeui.domain.usecase.chat

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.model.chat.Message
import com.ducpv.composeui.domain.repository.FireStoreRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 15/05/2023.
 */
@Singleton
class SubscribeMessagesUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val fireStoreRepository: FireStoreRepository,
) {
    private var subscribeMessagesScope: CoroutineScope? = null

    private val _subscribeMessages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val subscribeMessages: Flow<List<Message>>
        get() = _subscribeMessages

    fun startSubscribeMessage(rid: String, page: Int = 0, limit: Long = 20L) {
        subscribeMessagesScope = CoroutineScope(SupervisorJob() + dispatcher.io)
        subscribeMessagesScope?.launch {
            fireStoreRepository.subscribeMessages(rid, page, limit).collect {
                _subscribeMessages.emit(
                    _subscribeMessages.value.toMutableList().apply {
                        addAll(it)
                    },
                )
            }
        }
    }

    suspend fun removeSubscribeMessages() {
        subscribeMessagesScope?.cancel()
        subscribeMessagesScope = null
        _subscribeMessages.emit(emptyList())
    }
}
