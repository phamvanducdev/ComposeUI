package com.ducpv.composeui.domain.repository

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.firestore.FireStoreReference
import com.ducpv.composeui.domain.model.ChatRoom
import com.ducpv.composeui.domain.model.RoomType
import com.ducpv.composeui.domain.model.UserInfo
import com.ducpv.composeui.shared.extension.asyncAll
import com.ducpv.composeui.shared.extension.split
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Created by pvduc9773 on 24/05/2023.
 */
interface FireStoreRepository {
    suspend fun setUserInfo(userInfo: UserInfo)
    suspend fun getUserInfo(uid: String): UserInfo?
    suspend fun getChatRooms(roomType: RoomType): Flow<List<ChatRoom>>
}

class FireStoreRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val dispatcher: AppDispatcher,
) : BaseFireStoreRepository(dispatcher), FireStoreRepository {
    override suspend fun setUserInfo(userInfo: UserInfo): Unit = withContext(dispatcher.io) {
        fireStore
            .collection(FireStoreReference.USERS)
            .add(userInfo)
            .await()
    }

    override suspend fun getUserInfo(uid: String): UserInfo? = withContext(dispatcher.io) {
        fireStore
            .collection(FireStoreReference.USERS)
            .whereEqualTo(FireStoreReference.UID, uid)
            .get()
            .await()
            .toObjects(UserInfo::class.java)
            .firstOrNull()
    }

    override suspend fun getChatRooms(roomType: RoomType) = callbackFlow {
        val registrationListener = fireStore
            .collection(FireStoreReference.ROOMS)
            .whereEqualTo(FireStoreReference.TYPE, roomType)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    cancel()
                    return@addSnapshotListener
                }
                snapshot?.documents?.let {
                    trySend(it)
                }
            }
        awaitClose {
            registrationListener.remove()
        }
    }.conflate().mapTo(ChatRoom::class.java).flowOn(dispatcher.io)
}

abstract class BaseFireStoreRepository(private val dispatcher: AppDispatcher) {
    private val deserializePoolSize = Runtime.getRuntime().availableProcessors().coerceAtLeast(2)

    suspend fun <T> Flow<List<DocumentSnapshot>>.mapTo(clazz: Class<T>): Flow<List<T>> =
        map { documents ->
            documents
                .split(deserializePoolSize)
                .asyncAll(dispatcher.compute) { chunked ->
                    chunked.mapNotNull { doc ->
                        doc.toObject(clazz)
                    }
                }.flatten()
        }
}
