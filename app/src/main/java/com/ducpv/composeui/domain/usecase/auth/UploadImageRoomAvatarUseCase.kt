package com.ducpv.composeui.domain.usecase.auth

import android.net.Uri
import com.ducpv.composeui.domain.firestore.model.toFireStoreRoom
import com.ducpv.composeui.domain.model.chat.Room
import com.ducpv.composeui.domain.repository.FireStoreRepository
import com.ducpv.composeui.domain.repository.StorageRepository
import com.ducpv.composeui.shared.utility.FirebaseStoragePath
import javax.inject.Inject

/**
 * Created by pvduc9773 on 11/06/2023.
 */
class UploadImageRoomAvatarUseCase @Inject constructor(
    private val fireStoreRepository: FireStoreRepository,
    private val storageRepository: StorageRepository,
) {
    suspend operator fun invoke(imageUri: Uri, room: Room) {
        val downloadUrl = storageRepository.uploadImageToFirebaseStorage(
            imageUri = imageUri,
            path = String.format(FirebaseStoragePath.RoomAvatar.path, room.rid),
        )
        val newRoom = room.copy(
            avatar = downloadUrl.toString(),
        )
        fireStoreRepository.insertRoom(newRoom.toFireStoreRoom())
    }
}
