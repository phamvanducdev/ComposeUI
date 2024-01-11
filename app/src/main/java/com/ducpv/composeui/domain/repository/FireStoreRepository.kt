package com.ducpv.composeui.domain.repository

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.firestore.model.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
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
    suspend fun getUser(uid: String): FireStoreUser?
    suspend fun getUsers(uids: List<String>): List<FireStoreUser>
    suspend fun insertUser(data: FireStoreUser)

    suspend fun subscribeRooms(types: List<String>, limit: Long): Flow<List<FireStoreRoom>>
    suspend fun insertRoom(data: FireStoreRoom)
    suspend fun updateRoom(data: FireStoreRoom)
    suspend fun getRoom(rid: String): FireStoreRoom?

    suspend fun subscribeMessages(rid: String, limit: Long): Flow<List<FireStoreMessage>>
    suspend fun insertMessage(data: FireStoreMessage): DocumentReference
}

class FireStoreRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val dispatcher: AppDispatcher,
) : FireStoreRepository {
    override suspend fun getUser(uid: String): FireStoreUser? = withContext(dispatcher.io) {
        firebaseFirestore
            .collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(FireStoreUser::class.java)
    }

    override suspend fun getUsers(uids: List<String>): List<FireStoreUser> = withContext(dispatcher.io) {
        if (uids.isEmpty()) return@withContext emptyList()
        firebaseFirestore
            .collection("users")
            .whereIn(FieldPath.documentId(), uids)
            .get()
            .await()
            .toObjects(FireStoreUser::class.java)
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
                    snapshot.toObjects(FireStoreRoom::class.java),
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

    override suspend fun updateRoom(data: FireStoreRoom) {
        firebaseFirestore
            .collection("rooms")
            .document(data.rid)
            .set(data)
            .await()
    }

    override suspend fun getRoom(rid: String): FireStoreRoom? = withContext(dispatcher.io) {
        firebaseFirestore
            .collection("rooms")
            .document(rid)
            .get()
            .await()
            .toObject(FireStoreRoom::class.java)
    }

    override suspend fun subscribeMessages(rid: String, limit: Long) = callbackFlow {
        val subscription = firebaseFirestore
            .collection("rooms")
            .document(rid)
            .collection("messages")
            .orderBy(FireStoreMessage::createdAt.name, Query.Direction.DESCENDING)
            .limit(limit)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || snapshot.isEmpty || snapshot.documentChanges.isEmpty()) {
                    return@addSnapshotListener
                }
                trySend(
                    snapshot.toObjects(FireStoreMessage::class.java),
                )
            }
        awaitClose { subscription.remove() }
    }.flowOn(dispatcher.io)

    override suspend fun insertMessage(data: FireStoreMessage): DocumentReference = withContext(dispatcher.io) {
        firebaseFirestore
            .collection("rooms")
            .document(data.rid)
            .collection("messages")
            .add(data) // Auto-generate document ID
            .await()
    }
}
