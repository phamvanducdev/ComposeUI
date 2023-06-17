package com.ducpv.composeui.domain.repository

import android.net.Uri
import com.ducpv.composeui.core.util.AppDispatcher
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Created by pvduc9773 on 11/06/2023.
 */
interface StorageRepository {
    suspend fun uploadImageToFirebaseStorage(imageUri: Uri, path: String): Uri?
}

class StorageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val dispatcher: AppDispatcher,
) : StorageRepository {
    override suspend fun uploadImageToFirebaseStorage(imageUri: Uri, path: String): Uri? =
        withContext(dispatcher.io) {
            firebaseStorage
                .reference
                .child(path)
                .putFile(imageUri).await()
                .storage.downloadUrl.await()
        }
}
