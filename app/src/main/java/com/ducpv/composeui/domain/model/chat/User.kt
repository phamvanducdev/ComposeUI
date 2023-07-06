package com.ducpv.composeui.domain.model.chat

import java.util.*

/**
 * Created by pvduc9773 on 24/05/2023.
 */
data class User(
    val uid: String,
    val email: String,
    val name: String?,
    val avatar: String?,
    val createdAt: Date,
    val updatedAt: Date,
)
