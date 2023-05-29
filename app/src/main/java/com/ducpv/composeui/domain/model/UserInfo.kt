package com.ducpv.composeui.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

/**
 * Created by pvduc9773 on 24/05/2023.
 */
@Serializable
data class UserInfo(
    @SerializedName("uid") val uid: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("name") val name: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
)
