package com.ducpv.composeui.domain.usecase.chat

import com.ducpv.composeui.domain.firestore.model.toRoom
import com.ducpv.composeui.domain.model.chat.Room
import com.ducpv.composeui.domain.repository.FireStoreRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by pvduc9773 on 17/06/2023.
 */
class GetRoomInfoUseCase @Inject constructor(
    private val fireStoreRepository: FireStoreRepository,
) {
    private val _roomInfo: MutableStateFlow<Room?> = MutableStateFlow(null)
    val roomInfo: Flow<Room?>
        get() = _roomInfo

    suspend operator fun invoke(rid: String) {
        _roomInfo.emit(
            fireStoreRepository.getRoom(rid)?.toRoom(),
        )
    }
}
