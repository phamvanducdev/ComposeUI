package com.ducpv.composeui.domain.usecase.chat

import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.ducpv.composeui.domain.firestore.model.FireStoreMessage
import com.ducpv.composeui.domain.firestore.model.FireStoreNewestMessage
import com.ducpv.composeui.domain.model.chat.MessageType
import com.ducpv.composeui.domain.repository.FireStoreRepository
import java.util.*
import javax.inject.Inject

/**
 * Created by pvduc9773 on 17/06/2023.
 */
class SendMessageUseCase @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val fireStoreRepository: FireStoreRepository,
) {
    suspend operator fun invoke(
        rid: String,
        content: String,
        type: MessageType,
    ) {
        val uid = authDataStore.getUserInfo()?.uid ?: throw Exception("Not found user info!")
        val fireStoreMessage = FireStoreMessage(
            uid = uid,
            rid = rid,
            content = content,
            type = type.value,
            createdAt = Date(),
        )
        val mid = fireStoreRepository.insertMessage(fireStoreMessage).id
        val fireStoreRoom = fireStoreRepository.getRoom(rid) ?: throw Exception("Not found room info!")
        val fireStoreRoomUpdated = fireStoreRoom.copy(
            uids = fireStoreRoom.uids
                .toMutableList()
                .apply { add(uid) }
                .distinct(),
            newestMessage = FireStoreNewestMessage(
                mid = mid,
                uid = fireStoreMessage.uid,
                content = fireStoreMessage.content,
                type = fireStoreMessage.type,
                createdAt = fireStoreMessage.createdAt,
            ),
            updatedAt = fireStoreMessage.createdAt,
        )
        fireStoreRepository.updateRoom(fireStoreRoomUpdated)
    }
}
