package com.ducpv.composeui.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

/**
 * Created by pvduc9773 on 24/05/2023.
 */
data class ChatRoom(
    @DocumentId val documentId: String = "",
    val name: String = "",
    val type: RoomType = RoomType.PUBLIC,
)

enum class RoomType {
    @PropertyName("public") PUBLIC,
    @PropertyName("private") PRIVATE,
}
