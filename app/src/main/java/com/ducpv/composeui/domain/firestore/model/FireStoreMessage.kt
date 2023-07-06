package com.ducpv.composeui.domain.firestore.model

import com.ducpv.composeui.domain.model.chat.Message
import com.ducpv.composeui.domain.model.chat.MessageType
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * Created by pvduc9773 on 11/06/2023.
 */
data class FireStoreMessage(
    @DocumentId val mid: String = "",
    val uid: String = "",
    val rid: String = "",
    val content: String = "",
    val type: String = "",
    @ServerTimestamp val createdAt: Date? = null,
)

fun FireStoreMessage.toMessage(): Message =
    Message(
        mid = mid,
        uid = uid,
        rid = rid,
        content = content,
        type = MessageType.convertBy(type),
        createdAt = createdAt ?: Date(),
    )
