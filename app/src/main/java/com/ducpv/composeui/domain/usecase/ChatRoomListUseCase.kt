package com.ducpv.composeui.domain.usecase

import com.ducpv.composeui.domain.model.ChatRoom
import com.ducpv.composeui.domain.model.RoomType
import com.ducpv.composeui.domain.model.RunTracker
import com.ducpv.composeui.domain.repository.FireStoreRepository
import com.ducpv.composeui.domain.repository.RunTrackerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by pvduc9773 on 15/05/2023.
 */
class ChatRoomListUseCase @Inject constructor(
    private val fireStoreRepository: FireStoreRepository
) {
    suspend operator fun invoke(roomType: RoomType): Flow<List<ChatRoom>> = fireStoreRepository.getChatRooms(roomType)
}
