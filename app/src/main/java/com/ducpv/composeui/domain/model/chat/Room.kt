package com.ducpv.composeui.domain.model.chat

import java.util.*

/**
 * Created by pvduc9773 on 24/05/2023.
 */
data class Room(
    val rid: String = "",
    val name: String = "",
    val type: RoomType = RoomType.Public,
    val uids: List<String> = emptyList(),
    val avatar: String? = null,
    val newestMessage: NewestMessage? = null,
    val createdAt: Date,
    val updatedAt: Date,
)

data class NewestMessage(
    val mid: String,
    val uid: String,
    val content: String,
    val type: MessageType,
    val createdAt: Date,
)

enum class RoomType(val value: String) {
    Public("public"),
    Private("private");

    companion object {
        fun convertBy(type: String): RoomType {
            return RoomType.values().firstOrNull { it.value == type } ?: Public
        }
    }
}
