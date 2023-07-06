package com.ducpv.composeui.domain.model.chat

import java.util.Date

/**
 * Created by pvduc9773 on 11/06/2023.
 */
data class Message(
    val mid: String,
    val uid: String,
    val rid: String,
    val content: String,
    val type: MessageType,
    val createdAt: Date,
)

enum class MessageType(val value: String) {
    Text("text"),
    Image("image");

    companion object {
        fun convertBy(type: String): MessageType {
            return MessageType.values().firstOrNull { it.value == type } ?: Text
        }
    }
}
