package com.ducpv.composeui.domain.firestore.model

import com.ducpv.composeui.domain.model.chat.User
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * Created by pvduc9773 on 10/06/2023.
 */
class FireStoreUser(
    @DocumentId val uid: String = "",
    val email: String = "",
    val name: String? = null,
    val avatar: String? = null,
    @ServerTimestamp val createdAt: Date? = null,
    @ServerTimestamp val updatedAt: Date? = null,
)

fun FireStoreUser.toUser(): User =
    User(
        uid = uid,
        email = email,
        name = name,
        avatar = avatar,
        createdAt = createdAt ?: Date(),
        updatedAt = updatedAt ?: Date(),
    )

fun User.toFireStoreUser(): FireStoreUser =
    FireStoreUser(
        uid = uid,
        email = email,
        name = name,
        avatar = avatar,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
