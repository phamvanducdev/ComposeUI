package com.ducpv.composeui.domain.repository

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.firestore.model.*
import com.ducpv.composeui.domain.model.chat.Message
import com.ducpv.composeui.domain.model.chat.Room
import com.ducpv.composeui.domain.model.chat.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Created by pvduc9773 on 24/05/2023.
 */
interface FireStoreRepository {
    suspend fun getUser(uid: String): User?
    suspend fun insertUser(data: FireStoreUser)

    suspend fun subscribeRooms(types: List<String>, limit: Long): Flow<List<Room>>
    suspend fun insertRoom(data: FireStoreRoom)

    suspend fun subscribeMessages(rid: String, page: Int, limit: Long): Flow<List<Message>>
    suspend fun insertMessage(data: FireStoreMessage): String
}

class FireStoreRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val dispatcher: AppDispatcher,
) : FireStoreRepository {
    override suspend fun getUser(uid: String): User? = withContext(dispatcher.io) {
        firebaseFirestore
            .collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(FireStoreUser::class.java)?.toUser()
    }

    override suspend fun insertUser(data: FireStoreUser): Unit = withContext(dispatcher.io) {
        firebaseFirestore
            .collection("users")
            .document(data.uid)
            .set(data)
            .await()
    }

    override suspend fun subscribeRooms(types: List<String>, limit: Long) = callbackFlow {
        val subscription = firebaseFirestore
            .collectionGroup("rooms")
            .whereIn(FireStoreRoom::type.name, types)
            .orderBy(FireStoreRoom::createdAt.name, Query.Direction.DESCENDING)
            .limit(limit)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || snapshot.isEmpty || snapshot.documentChanges.isEmpty()) {
                    return@addSnapshotListener
                }
                trySend(
                    snapshot.toObjects(FireStoreRoom::class.java).map { it.toRoom() },
                )
            }
        awaitClose { subscription.remove() }
    }.flowOn(dispatcher.io)

    override suspend fun insertRoom(data: FireStoreRoom) {
        firebaseFirestore
            .collection("rooms")
            .add(data) // Auto-generate document ID
            .await()
    }

    override suspend fun subscribeMessages(rid: String, page: Int, limit: Long) = callbackFlow {
        val subscription = firebaseFirestore
            .collection("rooms")
            .document(rid)
            .collection("messages")
            .orderBy(FireStoreMessage::createdAt.name, Query.Direction.DESCENDING)
            .startAt(page * limit)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || snapshot.isEmpty || snapshot.documentChanges.isEmpty()) {
                    return@addSnapshotListener
                }
                trySend(
                    snapshot.toObjects(FireStoreMessage::class.java).map { it.toMessage() },
                )
            }
        awaitClose { subscription.remove() }
    }.flowOn(dispatcher.io)

    override suspend fun insertMessage(data: FireStoreMessage): String =
        firebaseFirestore
            .collection("rooms")
            .document(data.rid)
            .collection("messages")
            .add(data) // Auto-generate document ID
            .await()
            .id
}
