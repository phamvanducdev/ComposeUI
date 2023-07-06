package com.ducpv.composeui.domain.usecase.chat

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.firestore.model.toRoom
import com.ducpv.composeui.domain.model.chat.Room
import com.ducpv.composeui.domain.model.chat.RoomType
import com.ducpv.composeui.domain.repository.FireStoreRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by pvduc9773 on 15/05/2023.
 */
@Singleton
class SubscribeRoomsUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val fireStoreRepository: FireStoreRepository,
) {
    companion object {
        enum class SubscribeRoomsType {
            Public, Private, All;

            fun getTypes(): List<String> {
                return when (this) {
                    Public -> listOf(RoomType.Public)
                    Private -> listOf(RoomType.Private)
                    All -> listOf(
                        RoomType.Public,
                        RoomType.Private,
                    )
                }.map { it.value }
            }
        }
    }

    private var subscribeRoomsScope: CoroutineScope? = null

    private val _subscribeRooms: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())
    val subscribeRooms: Flow<List<Room>>
        get() = _subscribeRooms

    fun startSubscribeRooms(type: SubscribeRoomsType, limit: Long = 100L) {
        subscribeRoomsScope = CoroutineScope(SupervisorJob() + dispatcher.io)
        subscribeRoomsScope?.launch {
            fireStoreRepository.subscribeRooms(type.getTypes(), limit).collect { fireStoreRooms ->
                _subscribeRooms.emit(
                    fireStoreRooms.map { it.toRoom() },
                )
            }
        }
    }

    fun removeSubscribeRooms() {
        subscribeRoomsScope?.launch {
            _subscribeRooms.emit(emptyList())
            cancel()
        }
    }
}
