package com.ducpv.composeui.domain.firestore.model

import com.ducpv.composeui.domain.model.chat.MessageType
import com.ducpv.composeui.domain.model.chat.NewestMessage
import com.ducpv.composeui.domain.model.chat.Room
import com.ducpv.composeui.domain.model.chat.RoomType
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * Created by pvduc9773 on 10/06/2023.
 */
class FireStoreRoom(
    @DocumentId val rid: String = "",
    val name: String = "",
    val type: String = "",
    val uids: List<String> = emptyList(),
    val avatar: String? = null,
    @ServerTimestamp val createdAt: Date? = null,
    @ServerTimestamp var updatedAt: Date? = null,
    val newestMessage: FireStoreNewestMessage? = null,
)

data class FireStoreNewestMessage(
    val mid: String = "",
    val uid: String = "",
    val content: String = "",
    val type: String = "",
    val createdAt: Date? = null,
)

fun FireStoreRoom.toRoom(): Room =
    Room(
        rid = rid,
        name = name,
        type = RoomType.convertBy(type),
        uids = uids,
        avatar = avatar,
        newestMessage = newestMessage?.toNewestMessage(),
        createdAt = createdAt ?: Date(),
        updatedAt = updatedAt ?: Date(),
    )

fun Room.toFireStoreRoom(): FireStoreRoom =
    FireStoreRoom(
        rid = rid,
        name = name,
        type = type.value,
        uids = uids,
        avatar = avatar,
        newestMessage = newestMessage?.toFireStoreNewestMessage(),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun FireStoreNewestMessage.toNewestMessage(): NewestMessage =
    NewestMessage(
        mid = mid,
        uid = uid,
        content = content,
        type = MessageType.convertBy(type),
        createdAt = createdAt ?: Date(),
    )

fun NewestMessage.toFireStoreNewestMessage(): FireStoreNewestMessage =
    FireStoreNewestMessage(
        mid = mid,
        uid = uid,
        content = content,
        type = type.value,
        createdAt = createdAt,
    )
