package com.ducpv.composeui.domain.usecase.auth

import android.net.Uri
import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.ducpv.composeui.domain.datastore.toDataStoreUser
import com.ducpv.composeui.domain.firestore.model.toFireStoreUser
import com.ducpv.composeui.domain.repository.FireStoreRepository
import com.ducpv.composeui.domain.repository.StorageRepository
import com.ducpv.composeui.shared.utility.FirebaseStoragePath
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.withContext

/**
 * Created by pvduc9773 on 11/06/2023.
 */
class UploadImageUserAvatarUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val authDataStore: AuthDataStore,
    private val fireStoreRepository: FireStoreRepository,
    private val storageRepository: StorageRepository,
) {
    suspend operator fun invoke(imageUri: Uri) {
        val userInfo = authDataStore.getUserInfo() ?: throw Exception("Not found user info!")
        val downloadUrl = storageRepository.uploadImageToFirebaseStorage(
            imageUri = imageUri,
            path = String.format(FirebaseStoragePath.UserAvatar.path, userInfo.uid),
        )
        val newUserInfo = userInfo.copy(
            avatar = downloadUrl.toString(),
            updatedAt = Date(),
        )
        fireStoreRepository.insertUser(newUserInfo.toFireStoreUser())
        withContext(dispatcher.io) {
            authDataStore.setDataStoreUser(newUserInfo.toDataStoreUser())
        }
    }
}
