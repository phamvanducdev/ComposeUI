package com.ducpv.composeui.shared.utility

/**
 * Created by pvduc9773 on 11/06/2023.
 */
object FirebaseStorageUtility

enum class FirebaseStoragePath(val path: String) {
    UserAvatar("images/users/avatars/%s"),
    RoomAvatar("images/rooms/avatars/%s"),
}
