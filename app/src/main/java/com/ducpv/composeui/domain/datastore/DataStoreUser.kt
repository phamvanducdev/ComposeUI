package com.ducpv.composeui.domain.datastore

import com.ducpv.composeui.domain.model.chat.User
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Created by pvduc9773 on 10/06/2023.
 */
@Serializable
data class DataStoreUser(
    val uid: String,
    val email: String,
    val name: String?,
    val avatar: String?,
    val createdAt: Long,
    val updatedAt: Long,
)

fun DataStoreUser.toUser(): User =
    User(
        uid = uid,
        email = email,
        name = name,
        avatar = avatar,
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt),
    )

fun User.toDataStoreUser(): DataStoreUser =
    DataStoreUser(
        uid = uid,
        email = email,
        name = name,
        avatar = avatar,
        createdAt = createdAt.time,
        updatedAt = updatedAt.time,
    )
